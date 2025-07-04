import { Routes, Route } from "react-router-dom";
import Home from "./pages/Home";
import About from "./pages/About";
import BrowseDocumentsPage from "./pages/BrowseDocumentsPage";
import DocumentPage from "./pages/DocumentPage";
import LoginComponent from './auth/LoginComponent';
import DashboardComponent from './auth/DashboardComponent';

export default function App() {
  return (
    <Routes>
      <Route path="/" element={<LoginComponent />} />
      <Route path="/home" element={<Home />} />
      <Route path="/about" element={<About />} />
      <Route path="/documents" element={<BrowseDocumentsPage />} />
      <Route path="documents/:id" element={<DocumentPage />} />

      <Route path="/login" element={<LoginComponent />} />
      <Route path="/dashboard" element={<DashboardComponent />} />
    </Routes>
  );
}
