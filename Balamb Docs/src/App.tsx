import "./App.css";
import { Routes, Route, useLocation } from "react-router-dom";
import { lazy, Suspense, useRef } from 'react';
import { SwitchTransition, CSSTransition } from 'react-transition-group';

// Eagerly loaded components
import Home from "./pages/Home";
import LoginPage from './pages/LoginPage';
import RegisterPage from './pages/RegisterPage';
import NavbarWrapper from './pages/NavbarWrapper';
import UserProfile from "./pages/UserProfile";
import RequireAuth from './auth/RequireAuth';
import LoadingPage from './pages/LoadingPage'

// Lazy-loaded components
const About = lazy(() => import('./pages/About'));
const BrowseDocumentsPage = lazy(() => import('./pages/BrowseDocumentsPage'));
const DocumentPage = lazy(() => import('./pages/DocumentPage'));
const NotFoundPage = lazy(() => import('./pages/NotFoundPage'));
const Settings = lazy(() => import('./pages/UserInfoPage'));

export default function App() {
  const location = useLocation();
  const nodeRef = useRef(null);

  return (
    <div className="app-wrapper">
      <SwitchTransition>
        <CSSTransition
          key={location.pathname}
          classNames="fade"
          timeout={250}
          nodeRef={nodeRef}
          unmountOnExit
        >
          <div ref={nodeRef} className="page-transition">
            <Suspense fallback={<LoadingPage />}>
              <Routes location={location}>
                {/* Public */}
                <Route path="/login" element={<LoginPage />} />
                <Route path="/register" element={<RegisterPage />} />

                {/* Protected */}
                <Route element={<RequireAuth />}>
                  <Route element={<NavbarWrapper />}>
                    <Route path="/" element={<Home />} />
                    <Route path="/home" element={<Home />} />
                    <Route path="/about" element={<About />} />
                    <Route path="/profile/:id" element={<UserProfile />} />
                    <Route path="/documents" element={<BrowseDocumentsPage />} />
                    <Route path="/documents/:id" element={<DocumentPage />} />
                    <Route path="/accountInfo/:id" element={<Settings />} />
                  </Route>
                </Route>

                {/* 404 */}
                <Route path="*" element={<NotFoundPage />} />
              </Routes>
            </Suspense>
          </div>
        </CSSTransition>
      </SwitchTransition>
    </div>
  );
}
