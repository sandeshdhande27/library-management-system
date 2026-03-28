"use client";

import { useEffect, useState } from "react";
import { useRouter } from "next/navigation";
import toast from "react-hot-toast";

export default function Dashboard() {
  const router = useRouter();

  const [books, setBooks] = useState([]);
  const [categories, setCategories] = useState([]);
  const [category, setCategory] = useState("");

  const [token, setToken] = useState(null);
  const [user, setUser] = useState(null);

  const [loading, setLoading] = useState(true);
  const [actionLoading, setActionLoading] = useState(false);

  const [selectedBook, setSelectedBook] = useState(null);
  const [days, setDays] = useState(5);
  const [showTakeModal, setShowTakeModal] = useState(false);
   const [dateTime, setDateTime] = useState("");

   const [showHistory, setShowHistory] = useState(false);
const [showReport, setShowReport] = useState(false);

const [history, setHistory] = useState(null);
const [topCategories, setTopCategories] = useState([]);

const [loadingHistory, setLoadingHistory] = useState(false);
const [loadingReport, setLoadingReport] = useState(false);

  // 🔐 AUTH + USER FETCH
useEffect(() => {
  const t = localStorage.getItem("jwt_lib_token");
  const email = localStorage.getItem("user_email");

  if (!t || !email) {
    router.replace("/");
    return;
  }

  setToken(t);

  fetch(`http://localhost:8080/api/users/get_user_information?email=${email}`, {
    headers: { Authorization: `Bearer ${t}` },
  })
    .then((res) => res.json())
    .then((data) => setUser(data))
    .catch(() => {
      toast.error("Failed to load user");
      router.replace("/");
    });

}, []);


useEffect(() => {
  const interval = setInterval(() => {
    const now = new Date();

    const formatted = now.toLocaleString("en-IN", {
      weekday: "long",      // Monday
      year: "numeric",
      month: "short",
      day: "numeric",
      hour: "2-digit",
      minute: "2-digit",
      second: "2-digit",
      hour12: false         // 🔥 24-hour format
    });

    setDateTime(formatted);
  }, 1000);

  return () => clearInterval(interval);
}, []);

  const headers = {
    "Content-Type": "application/json",
    Authorization: `Bearer ${token}`,
  };




const loadUserHistory = async () => {
  try {
    setLoadingHistory(true);

    const res = await fetch(
      `http://localhost:8080/api/users/${user.id}/history`,
      {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      }
    );

    const data = await res.json();
    setHistory(data);
    setShowHistory(true);
    setShowReport(false);
  } catch (err) {
    console.error(err);
  } finally {
    setLoadingHistory(false);
  }
};

const loadTopCategory = async () => {
  try {
    setLoadingReport(true);

    const res = await fetch(
      `http://localhost:8080/api/reports/top-category`,
      {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      }
    );

    const data = await res.json();
    setTopCategories(data);
    setShowReport(true);
    setShowHistory(false);
  } catch (err) {
    console.error(err);
  } finally {
    setLoadingReport(false);
  }
};


  // 📚 LOAD BOOKS
  const loadBooks = async () => {
    try {
      setLoading(true);
      const res = await fetch("http://localhost:8080/api/books/get_books", { headers });
      setBooks(await res.json());
    } catch {
      toast.error("Failed to load books");
    } finally {
      setLoading(false);
    }
  };

  // 📂 LOAD CATEGORIES
  const loadCategories = async () => {
    try {
      const res = await fetch("http://localhost:8080/api/books/categories", { headers });
      setCategories(await res.json());
    } catch {
      toast.error("Failed to load categories");
    }
  };

  useEffect(() => {
    if (token) {
      loadBooks();
      loadCategories();
    }
  }, [token]);

  // 🔍 FILTER
  const filterBooks = async () => {
    try {
      setLoading(true);

      const res = await fetch(
        "http://localhost:8080/api/books/select_specific_book",
        {
          method: "POST",
          headers,
          body: JSON.stringify(category ? { bookCategory: category } : {}),
        }
      );

      setBooks(await res.json());
    } catch {
      toast.error("Filter failed");
    } finally {
      setLoading(false);
    }
  };

  // 🔄 RESET
  const resetBooks = async () => {
    setCategory("");
    loadBooks();
  };

  // 📥 TAKE BOOK
  const confirmTakeBook = async () => {
    if (days <= 0) return toast.error("Invalid days");
    if (!user?.id) return toast.error("User not loaded");

    try {
      setActionLoading(true);

      const res = await fetch("http://localhost:8080/api/books/take_book", {
        method: "POST",
        headers,
        body: JSON.stringify({
          bookId: selectedBook,
          userId: user.id,
          days: Number(days),
        }),
      });

      toast.success(await res.text());

      setShowTakeModal(false);
      setSelectedBook(null);
      setDays(5);
      loadBooks();

    } catch {
      toast.error("Take failed");
    } finally {
      setActionLoading(false);
    }
  };

  // 📤 RETURN BOOK
  const returnBook = async (bookId) => {
    try {
      setActionLoading(true);

      const res = await fetch("http://localhost:8080/api/books/return_book", {
        method: "POST",
        headers,
        body: JSON.stringify({ bookId }),
      });

      toast.success(await res.text());
      loadBooks();

    } catch {
      toast.error("Return failed");
    } finally {
      setActionLoading(false);
    }
  };

  // 🚪 LOGOUT
  const logout = () => {
    localStorage.removeItem("jwt_lib_token");
    router.replace("/");
  };

  if (loading || !user) {
    return (
      <div className="h-screen flex items-center justify-center bg-black text-white">
        Loading dashboard...
      </div>
    );
  }


 


const closeModal = () => {
  setShowHistory(false);
  setShowReport(false);
};

return (
  <div className="min-h-screen p-5 bg-gradient-to-br from-black to-red-950 text-white">

    {/* HEADER */}
    <div className="flex justify-between items-center mb-6">
      <h1 className="text-2xl font-bold bg-gradient-to-r from-red-500 to-red-300 bg-clip-text text-transparent">
        📚 Library Dashboard
      </h1>

      <div className="flex items-center gap-3">

<button
  onClick={loadUserHistory}
  className="px-3 py-1 text-sm rounded bg-blue-600 hover:scale-105 transition"
>
  History
</button>

<button
  onClick={loadTopCategory}
  className="px-3 py-1 text-sm rounded bg-purple-600 hover:scale-105 transition"
>
  Reports
</button>

        <div className="flex gap-2">
          <button
            onClick={loadBooks}
            disabled={loading}
            className="px-3 py-1 text-sm rounded bg-red-600 hover:scale-105 transition"
          >
            Refresh
          </button>

          <button
            onClick={logout}
            className="px-3 py-1 text-sm rounded bg-red-900 hover:scale-105 transition"
          >
            Logout
          </button>
        </div>
        
        <div className="text-xs px-3 py-1 rounded bg-[#141414] border border-gray-700">
          {dateTime}
        </div>

        <div className="bg-[#141414] px-3 py-2 rounded border border-gray-700">
          <p className="text-sm font-semibold">{user?.name}</p>
          <p className="text-xs text-gray-400">{user?.email}</p>
        </div>



      </div>
    </div>

    {/* SUMMARY */}
    <div className="flex gap-4 mb-5">
      <div className="flex-1 p-4 text-center rounded bg-gradient-to-br from-red-700 to-red-900">
        <p>Total Books</p>
        <h2 className="text-xl font-bold">{books.length}</h2>
      </div>

      <div className="flex-1 p-4 text-center rounded bg-gradient-to-br from-purple-700 to-purple-900">
        <p>Categories</p>
        <h2 className="text-xl font-bold">{categories.length}</h2>
      </div>
    </div>

    {/* FILTER */}
    <div className="flex gap-3 mb-5">
      <select
        value={category}
        onChange={(e) => setCategory(e.target.value)}
        className="bg-[#141414] border border-gray-700 px-3 py-2 rounded text-white"
      >
        <option value="">All Categories</option>
        {categories.map((c) => (
          <option key={c} value={c}>{c}</option>
        ))}
      </select>

      <button
        onClick={filterBooks}
        className="px-3 py-2 rounded bg-red-600 hover:scale-105 transition"
      >
        Filter
      </button>

      <button
        onClick={resetBooks}
        className="px-3 py-2 rounded bg-gray-700 hover:scale-105 transition"
      >
        Reset
      </button>
    </div>

{(showHistory || showReport) && (
  <div className="fixed inset-0 bg-black/70 flex items-center justify-center z-50">

    <div className="bg-[#141414] w-[90%] max-w-2xl max-h-[80vh] overflow-y-auto rounded-xl border border-gray-700 p-5">

      {/* HEADER */}
      <div className="flex justify-between items-center mb-4">
        <h2 className={`text-lg font-semibold ${
          showHistory ? "text-blue-400" : "text-purple-400"
        }`}>
          {showHistory ? "📜 User History" : "📊 Top Categories"}
        </h2>

        <button
          onClick={closeModal}
          className="text-gray-400 hover:text-white text-lg"
        >
          ✖
        </button>
      </div>

      {/* ================= HISTORY ================= */}
      {showHistory && history && (
        <>
          {/* CURRENT BOOKS */}
          <div className="mb-4">
            <h3 className="text-sm text-gray-400 mb-2">Current Books</h3>

            {history.current_books.length === 0 ? (
              <p className="text-gray-500 text-sm">No current books</p>
            ) : (
              history.current_books.map((b, i) => (
                <div key={i} className="text-sm border-b border-gray-700 py-2">
                  {b.bookName} ({b.category})
                </div>
              ))
            )}
          </div>

          {/* PAST BOOKS */}
          <div>
            <h3 className="text-sm text-gray-400 mb-2">Past Books</h3>

            {history.past_books.map((b, i) => (
              <div key={i} className="text-sm border-b border-gray-700 py-2">
                <p className="font-medium">{b.bookName}</p>
                <p className="text-gray-400 text-xs">
                  {b.category} • {b.author}
                </p>
                <p className="text-green-400 text-xs">
                  {new Date(b.returnDate).toLocaleString("en-IN", {
                    hour12: false
                  })}
                </p>
              </div>
            ))}
          </div>
        </>
      )}

      {/* ================= REPORT ================= */}
      {showReport && (
        <div>
          {topCategories.map((c, i) => (
            <div key={i} className="mb-3">

              <div className="flex justify-between text-sm">
                <span>{c.category}</span>
                <span>{c.percentage}%</span>
              </div>

              <div className="w-full bg-gray-800 rounded h-2 mt-1">
                <div
                  className="bg-purple-600 h-2 rounded"
                  style={{ width: `${c.percentage}%` }}
                />
              </div>

            </div>
          ))}
        </div>
      )}

    </div>
  </div>
)}

    {/* BOOKS */}
    {books.length === 0 ? (
      <p className="text-center mt-10 text-gray-500">
        📭 No books available
      </p>
    ) : (
      <div className="grid grid-cols-[repeat(auto-fill,minmax(240px,1fr))] gap-5">
        {books.map((b) => (
          <div
            key={b.id}
            className="p-4 rounded-xl border border-gray-800 bg-gradient-to-br from-[#141414] to-[#0f0f0f] hover:-translate-y-1 hover:border-red-500 transition"
          >
            <div className="flex justify-between items-center">
              <h3 className="font-semibold">{b.bookName}</h3>

              <span
                className={`text-xs px-2 py-1 rounded ${
                  b.bookStatus === "AVAILABLE"
                    ? "bg-green-600"
                    : "bg-yellow-600"
                }`}
              >
                {b.bookStatus}
              </span>
            </div>

            <p className="text-xs text-gray-400 mt-1">
              {b.bookCategory}
            </p>

            <div className="mt-3">
              {b.bookStatus === "AVAILABLE" ? (
                <button
                  disabled={actionLoading}
                  onClick={() => {
                    setSelectedBook(b.id);
                    setShowTakeModal(true);
                  }}
                  className="w-full mt-2 px-3 py-2 rounded bg-green-600 hover:scale-105 transition"
                >
                  Take Book
                </button>
              ) : (
                <button
                  disabled={actionLoading}
                  onClick={() => returnBook(b.id)}
                  className="w-full mt-2 px-3 py-2 rounded bg-blue-600 hover:scale-105 transition"
                >
                  Return Book
                </button>
              )}
            </div>
          </div>
        ))}
      </div>
    )}

    {/* MODAL */}
    {showTakeModal && (
      <div className="fixed inset-0 bg-black/70 flex items-center justify-center">
        <div className="bg-[#141414] p-5 rounded-xl border border-gray-700 w-[280px]">
          <h2 className="text-red-400 mb-2">📖 Take Book</h2>

          <input
            type="number"
            value={days}
            onChange={(e) => setDays(Number(e.target.value))}
            className="w-full bg-black border border-gray-700 px-2 py-2 rounded text-white"
          />

          <div className="flex justify-end gap-2 mt-3">
            <button
              onClick={() => setShowTakeModal(false)}
              className="px-3 py-1 rounded bg-gray-700"
            >
              Cancel
            </button>

            <button
              onClick={confirmTakeBook}
              disabled={!days || days <= 0}
              className="px-3 py-1 rounded bg-green-600"
            >
              Confirm
            </button>
          </div>
        </div>
      </div>
    )}

  </div>
);
}