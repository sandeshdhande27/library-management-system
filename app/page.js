"use client";

import { useState } from "react";
import { useRouter } from "next/navigation";

export default function AuthPage() {
  const router = useRouter();

  const [isLogin, setIsLogin] = useState(true);
  const [loading, setLoading] = useState(false);

  // 🔐 ADMIN STATE
  const [showAdmin, setShowAdmin] = useState(false);
  const [isAdminLogged, setIsAdminLogged] = useState(false);
  const [adminPassword, setAdminPassword] = useState("");
  const [adminLoading, setAdminLoading] = useState(false);

  const [adminView, setAdminView] = useState("menu"); // menu | add | list
const [booksList, setBooksList] = useState([]);
const [loadingBooks, setLoadingBooks] = useState(false);

const [showUpdate, setShowUpdate] = useState(false);
const [editingId, setEditingId] = useState(null);
const [editBook, setEditBook] = useState({});

  // 📚 BOOK FORM
  const [bookForm, setBookForm] = useState({
    bookName: "",
    bookAuthor: "",
    bookCategory: "",
    bookStatus: "AVAILABLE",
  });

  const [addingBook, setAddingBook] = useState(false);

  const [form, setForm] = useState({
    name: "",
    email: "",
    password: "",
    membershipMonths: 6,
  });

  const handleChange = (e) => {
    setForm({ ...form, [e.target.name]: e.target.value });
  };

  const handleBookChange = (e) => {
    setBookForm({ ...bookForm, [e.target.name]: e.target.value });
  };


const loadBooksAdmin = async () => {
  try {
    setLoadingBooks(true);

    const token = localStorage.getItem("jwt_lib_token");

    const res = await fetch("http://localhost:8080/api/books/get_books", {
      headers: {
        Authorization: `Bearer ${token}`,
      },
    });

    const data = await res.json();

    // ✅ FIX: Ensure array
    if (Array.isArray(data)) {
      setBooksList(data);
    } else {
      console.error("Invalid response:", data);
      setBooksList([]); // fallback
    }

    setAdminView("list");

  } catch (err) {
    console.error(err);
    setBooksList([]); // fallback
  } finally {
    setLoadingBooks(false);
  }
};

const showToast = (message, type = "success") => {
  const toast = document.createElement("div");

  toast.innerText = message;

  toast.className = `
    fixed top-5 right-5 z-[9999]
    px-4 py-2 rounded-lg text-sm font-medium shadow-lg
    text-white transition-all duration-300
    ${type === "error" ? "bg-red-600" : "bg-green-600"}
  `;

  document.body.appendChild(toast);

  setTimeout(() => {
    toast.style.opacity = "0";
    toast.style.transform = "translateY(-10px)";
  }, 2000);

  setTimeout(() => {
    toast.remove();
  }, 2500);
};

const updateBook = async () => {
  try {
    const token = localStorage.getItem("jwt_lib_token");

    const res = await fetch(
      "http://localhost:8080/api/books/update_book",
      {
        method: "PUT",
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${token}`,
        },
        body: JSON.stringify({
          bookId: editBook.id,
          bookName: editBook.bookName,
          bookAuthor: editBook.bookAuthor,
          bookCategory: editBook.bookCategory,
        }),
      }
    );

    if (!res.ok) {
      showToast("Update failed ❌", "error");
      return;
    }

    showToast("Book updated successfully ✅");

    setEditingId(null);
    loadBooksAdmin();

  } catch (err) {
    console.error(err);
    showToast("Something went wrong ❌", "error");
  }
};

const deleteBook = async (id) => {
  try {
    const token = localStorage.getItem("jwt_lib_token");

    const res = await fetch(
      "http://localhost:8080/api/books/delete_book",
      {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${token}`,
        },
        body: JSON.stringify({ bookId: id }),
      }
    );

    if (!res.ok) {
      showToast("Delete failed ❌", "error");
      return;
    }

    showToast("Book deleted successfully 🗑️");

    loadBooksAdmin();

  } catch (err) {
    console.error(err);
    showToast("Something went wrong ❌", "error");
  }
};


  // ================= LOGIN =================
  const handleLogin = async () => {
    try {
      if (!form.email || !form.password) return alert("Email & Password required");

      setLoading(true);

      const res = await fetch("http://localhost:8080/api/auth/login", {
        method: "POST",
        headers: {"Content-Type": "application/json"},
        body: JSON.stringify({ email: form.email, password: form.password }),
      });

      const text = await res.text();

      if (!res.ok || !text.toLowerCase().includes("successful")) {
        alert("Login failed");
        return;
      }

      const jwtRes = await fetch("http://localhost:8080/api/auth/get_jwt", {
        method: "POST",
        headers: {"Content-Type": "application/json"},
        body: JSON.stringify({ username: "admin", password: "123" }),
      });

      const jwtData = await jwtRes.json();
      localStorage.setItem("jwt_lib_token", jwtData.token);

      router.push("/dashboard");

    } catch (err) {
      console.error(err);
    } finally {
      setLoading(false);
    }
  };

  // ================= ADMIN LOGIN =================
  const handleAdminLogin = async () => {
    try {
      setAdminLoading(true);

      if (adminPassword !== "admin@123") {
        alert("Invalid password");
        setAdminLoading(false);
        return;
      }

      const res = await fetch("http://localhost:8080/api/auth/get_jwt", {
        method: "POST",
        headers: {"Content-Type": "application/json"},
        body: JSON.stringify({ username: "admin", password: "123" }),
      });

      const data = await res.json();

      localStorage.setItem("jwt_lib_token", data.token);

      setIsAdminLogged(true); // 🔥 SWITCH PANEL
      setAdminPassword("");

    } catch (err) {
      console.error(err);
    } finally {
      setAdminLoading(false);
    }
  };

  // ================= ADD BOOK =================
const addBook = async () => {
  try {
    setAddingBook(true);

    const token = localStorage.getItem("jwt_lib_token");

    const res = await fetch("http://localhost:8080/api/books/add_books", {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
        Authorization: `Bearer ${token}`,
      },
      body: JSON.stringify(bookForm),
    });

    if (!res.ok) {
      showToast("Failed to add book ❌", "error");
      return;
    }

    showToast("Book added successfully ✅");

    setBookForm({
      bookName: "",
      bookAuthor: "",
      bookCategory: "",
      bookStatus: "AVAILABLE",
    });

  } catch (err) {
    console.error(err);
    showToast("Something went wrong ❌", "error");
  } finally {
    setAddingBook(false);
  }
};

return (
  <div className="min-h-screen flex flex-col items-center justify-center bg-gradient-to-br from-black via-red-950 to-black text-white px-4">

    {/* 🔥 ADMIN BUTTON */}
    <div className="mb-8">
      <button
        onClick={() => setShowAdmin(true)}
        className="px-8 py-3 text-lg font-semibold rounded-xl bg-gradient-to-r from-yellow-400 to-yellow-600 text-black shadow-lg hover:scale-105 transition"
      >
        👨‍💼 ADMIN PANEL
      </button>
    </div>

    {/* ================= ADMIN MODAL ================= */}
    {showAdmin && (
      <div className="fixed inset-0 bg-black/80 backdrop-blur-sm flex items-center justify-center z-50">

        <div className="w-[95%] max-w-4xl bg-gradient-to-br from-[#121212] to-[#1a1a1a] rounded-2xl shadow-2xl border border-gray-800">

          {/* HEADER */}
          <div className="flex justify-between items-center px-6 py-4 border-b border-gray-800">
            <h2 className="text-xl font-semibold">
              {isAdminLogged ? "Admin Panel" : "Admin Login"}
            </h2>

            <button
              onClick={() => {
                setShowAdmin(false);
                setIsAdminLogged(false);
              }}
              className="text-gray-400 hover:text-white text-lg"
            >
              ✖
            </button>
          </div>

          {/* BODY */}
          <div className="p-6">

            {/* LOGIN */}
            {!isAdminLogged ? (
              <div className="space-y-4 max-w-md mx-auto">

                <input
                  type="password"
                  placeholder="Enter Admin Password"
                  value={adminPassword}
                  onChange={(e) => setAdminPassword(e.target.value)}
                  className="w-full p-3 rounded-lg bg-black border border-gray-700 focus:border-yellow-500 outline-none"
                />

                <button
                  onClick={handleAdminLogin}
                  disabled={adminLoading}
                  className="w-full py-3 rounded-lg bg-yellow-500 hover:bg-yellow-600 text-black font-semibold transition"
                >
                  {adminLoading ? "Checking..." : "Login"}
                </button>

              </div>
            ) : (

              <div className="space-y-6">

                {/* BACK */}
                {adminView !== "menu" && (
                  <button
                    onClick={() => setAdminView("menu")}
                    className="text-sm text-gray-400 hover:text-white"
                  >
                    ← Back
                  </button>
                )}

                {/* MENU */}
                {adminView === "menu" && (
                  <div className="grid grid-cols-2 gap-6">

                    <div
                      onClick={() => setAdminView("add")}
                      className="p-6 rounded-xl bg-gradient-to-br from-green-600 to-green-800 hover:scale-105 transition cursor-pointer shadow-lg text-center"
                    >
                      ➕ Add Book
                    </div>

                    <div
                      onClick={loadBooksAdmin}
                      className="p-6 rounded-xl bg-gradient-to-br from-blue-600 to-blue-800 hover:scale-105 transition cursor-pointer shadow-lg text-center"
                    >
                      📚 View Books
                    </div>

                  </div>
                )}

                {/* ADD BOOK */}
    {adminView === "add" && (
      <div className="space-y-3">

        <h3 className="text-gray-300">Add Book</h3>

        <input
          name="bookName"
          placeholder="Book Name"
          value={bookForm.bookName}
          onChange={handleBookChange}
          className="w-full p-2 bg-black border rounded text-white"
        />

        <input
          name="bookAuthor"
          placeholder="Author"
          value={bookForm.bookAuthor}
          onChange={handleBookChange}
          className="w-full p-2 bg-black border rounded text-white"
        />

        <input
          name="bookCategory"
          placeholder="Category"
          value={bookForm.bookCategory}
          onChange={handleBookChange}
          className="w-full p-2 bg-black border rounded text-white"
        />

        <input
          value="AVAILABLE"
          disabled
          className="w-full p-2 bg-gray-800 border rounded text-gray-400"
        />

        <button
          onClick={addBook}
          className="w-full p-3 bg-green-600 rounded"
        >
          Add Book
        </button>

      </div>
    )}

                {/* BOOK TABLE */}
                {adminView === "list" && (
                  <div className="overflow-x-auto">

<table className="w-full text-sm rounded-xl overflow-hidden border border-gray-800">

  {/* HEADER */}
  <thead className="bg-gradient-to-r from-gray-800 to-gray-900 text-gray-200">
    <tr>
      <th className="p-3 text-left">Name</th>
      <th className="p-3 text-left">Author</th>
      <th className="p-3 text-left">Category</th>
      <th className="p-3 text-center">Action</th>
    </tr>
  </thead>

  {/* BODY */}
  <tbody>
    {booksList.map((b) => (
      <tr
        key={b.id}
        className="border-t border-gray-800 hover:bg-gray-900 transition"
      >

        {/* BOOK NAME */}
        <td className="p-3">
          {editingId === b.id ? (
            <input
              value={editBook.bookName}
              onChange={(e) =>
                setEditBook({ ...editBook, bookName: e.target.value })
              }
              className="bg-black border border-gray-600 text-white p-2 rounded w-full focus:border-yellow-500 outline-none"
            />
          ) : (
            <span className="font-medium">{b.bookName}</span>
          )}
        </td>

        {/* AUTHOR */}
        <td className="p-3">
          {editingId === b.id ? (
            <input
              value={editBook.bookAuthor}
              onChange={(e) =>
                setEditBook({ ...editBook, bookAuthor: e.target.value })
              }
              className="bg-black border border-gray-600 text-white p-2 rounded w-full"
            />
          ) : (
            <span className="text-gray-300">{b.bookAuthor}</span>
          )}
        </td>

        {/* CATEGORY */}
        <td className="p-3">
          {editingId === b.id ? (
            <input
              value={editBook.bookCategory}
              onChange={(e) =>
                setEditBook({ ...editBook, bookCategory: e.target.value })
              }
              className="bg-black border border-gray-600 text-white p-2 rounded w-full"
            />
          ) : (
            <span className="text-gray-400">{b.bookCategory}</span>
          )}
        </td>

        {/* ACTION */}
        <td className="p-3 text-center space-x-2">

          {editingId === b.id ? (
            <>
              <button
                onClick={updateBook}
                className="px-3 py-1 bg-green-600 hover:bg-green-700 rounded text-xs font-semibold transition"
              >
                Save
              </button>

              <button
                onClick={() => setEditingId(null)}
                className="px-3 py-1 bg-gray-600 hover:bg-gray-700 rounded text-xs transition"
              >
                Cancel
              </button>
            </>
          ) : (
            <>
              <button
                onClick={() => {
                  setEditingId(b.id);
                  setEditBook({ ...b });
                }}
                className="px-3 py-1 bg-yellow-500 hover:bg-yellow-600 text-black rounded text-xs font-semibold transition"
              >
                Edit
              </button>

              <button
                onClick={() => deleteBook(b.id)}
                className="px-3 py-1 bg-red-600 hover:bg-red-700 rounded text-xs font-semibold transition"
              >
                Delete
              </button>
            </>
          )}

        </td>

      </tr>
    ))}
  </tbody>
</table>

                  </div>
                )}

              </div>
            )}

          </div>
        </div>
      </div>
    )}


      {/* ================= LOGIN CARD ================= */}
      <div style={styles.card}>
        <div style={styles.header}>
          <h2 style={styles.title}>
            {isLogin ? "Welcome Back" : "Create Account"}
          </h2>
          <p style={styles.subtitle}> 
            {isLogin ? "Login to continue" : "Register to get started"}
          </p>
        </div>

        <input name="email" placeholder="Email"
          value={form.email}
          onChange={handleChange}
          style={styles.input}
        />

        <input type="password" name="password"
          placeholder="Password"
          value={form.password}
          onChange={handleChange}
          style={styles.input}
        />

        <button onClick={handleLogin} style={styles.button}>
          Login
        </button>
      </div>

  </div>
);
}

// styles 
const styles = {
  container: {
    height: "100vh",
    display: "flex",
    flexDirection: "column",
    justifyContent: "center",
    alignItems: "center",
    background: "linear-gradient(135deg, #0f0a0a, #1a0f0f)",
  },
  card: {
    background: "#121212",
    padding: "35px",
    borderRadius: "14px",
    width: "340px",
  },
  header: { textAlign: "center", marginBottom: "20px" },
  title: { color: "#fff", fontSize: "22px" },
  subtitle: { color: "#888", fontSize: "13px" },
  input: {
    width: "100%",
    padding: "10px",
    marginBottom: "10px",
    background: "#1a1a1a",
    color: "#fff",
  },
  button: {
    width: "100%",
    padding: "12px",
    background: "#dc2626",
    color: "#fff",
  },
};