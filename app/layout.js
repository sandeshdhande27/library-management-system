import { Toaster } from "react-hot-toast";
import "./globals.css";

export default function RootLayout({ children }) {
  return (
    <html>
      <body>
        <Toaster position="top-right" />
        {children}
      </body>
    </html>
  );
}