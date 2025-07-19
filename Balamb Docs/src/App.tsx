import { Routes, Route } from "react-router-dom";
import Home from "./pages/Home";
import About from "./pages/About";
import BrowseDocumentsPage from "./pages/BrowseDocumentsPage";
import DocumentPage from "./pages/DocumentPage";
import LoginPage from './pages/LoginPage';
import RequireAuth from './auth/RequireAuth';
import NavbarWrapper from './pages/NavbarWrapper.tsx';
import RegisterPage from './pages/RegisterPage';
import UserProfile from "./pages/UserProfile.tsx";
import NotFoundPage from "./pages/NotFoundPage";

export default function App() {
  return (
    <Routes>
      {/* Public */}
      <Route path="/login" element={<LoginPage />} />
      <Route path="/register" element={<RegisterPage />} />

      {/* Protected */}
      <Route element={<RequireAuth />}>
        <Route element={<NavbarWrapper />}>
          <Route path="/" element={<Home />} />
          <Route path="/profile/:id" element={<UserProfile />} />
          <Route path="/home" element={<Home />} />
          <Route path="/about" element={<About />} />
          <Route path="/documents" element={<BrowseDocumentsPage />} />
          <Route path="/documents/:id" element={<DocumentPage />} />
        </Route>
      </Route>

      {/* 404 Page Not Found */}
      <Route path="*" element={<NotFoundPage />} />
    </Routes>

  );
}
