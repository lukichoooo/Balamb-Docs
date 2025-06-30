import { Routes, Route } from "react-router-dom";
import Home from "./pages/Home";
import About from "./pages/About";
import BrowseDocumentsPage from "./pages/BrowseDocumentsPage";
import DocumentPage from "./pages/DocumentPage";

export default function App() {
  return (
    <Routes>
      <Route path="/" element={<Home />} />
      <Route path="/about" element={<About />} />
      <Route path="/documents" element={<BrowseDocumentsPage />} />
      <Route path="documents/:id" element={<DocumentPage />} />
    </Routes>
  );
}
