import React, { Suspense } from 'react';
import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom';
import { PrivateRoutes } from './components';
import { ROUTES } from './constants';
import { Layout } from './components/Layout';
import './tailwind/styles.css';
import CircularProgress from '@mui/material/CircularProgress';
import Dashboard from './pages/Dashboard';
import { useAuth } from './hooks';

// pages
const Login = React.lazy(() => import('./pages/Login'));
const Register = React.lazy(() => import('./pages/Register'));
const ConfirmRegistration = React.lazy(() => import('./pages/ConfirmRegistration'));
const Activity = React.lazy(() => import('./pages/Activity'));
const ActivityDetails = React.lazy(() => import('./pages/ActivityDetails'));
const Cards = React.lazy(() => import('./pages/Cards'));
const SendMoney = React.lazy(() => import('./pages/SendMoney'));
const LoadMoney = React.lazy(() => import('./pages/LoadMoney'));
const Profile = React.lazy(() => import('./pages/Profile'));
const PageNotFound = React.lazy(() => import('./pages/PageNotFound'));
const RecoverPassword = React.lazy(() => import('./pages/RecoverPassword'));
const ResetPassword = React.lazy(() => import('./pages/resetPassword')); 

function App() {
  const { isAuthenticated } = useAuth();

  return (
    <BrowserRouter>
      <Layout isAuthenticated={isAuthenticated}>
        <Suspense
          fallback={
            <div className="tw-w-full tw-h-full tw-flex tw-flex-col tw-items-center tw-justify-center">
              <CircularProgress />
            </div>
          }
        >
          <Routes>
            {/* Redirigir desde la raíz a /dashboard o a otra ruta deseada */}
            <Route path="/" element={<Navigate to={ROUTES.DASHBOARD} replace />} />
            
            {/* Rutas privadas */}
            <Route path={ROUTES.HOME} element={<PrivateRoutes />}>
              <Route element={<Dashboard />} path={ROUTES.HOME} />
              <Route element={<Dashboard />} path="/dashboard" /> {/* Mostrar el Dashboard en ambas rutas */}
              <Route element={<Activity />} path={ROUTES.ACTIVITY} />
              <Route element={<Cards />} path={ROUTES.CARDS} />
              <Route element={<SendMoney />} path={ROUTES.SEND_MONEY} />
              <Route element={<LoadMoney />} path={ROUTES.LOAD_MONEY} />
              <Route element={<Profile />} path={ROUTES.PROFILE} />
              <Route element={<ActivityDetails />} path={ROUTES.ACTIVITY_DETAILS} />
            </Route>

            {/* Rutas públicas */}
            <Route
              path={ROUTES.LOGIN}
              element={isAuthenticated ? <Navigate replace to="/dashboard" /> : <Login />}
            />
            <Route
              path={ROUTES.REGISTER}
              element={isAuthenticated ? <Navigate replace to="/dashboard" /> : <Register />}
            />
            <Route
              path={ROUTES.CONFIRM_REGISTRATION}
              element={isAuthenticated ? <Navigate replace to="/dashboard" /> : <ConfirmRegistration />}
            />
            <Route
            path={ROUTES.RECOVER_PASSWORD}
            element={isAuthenticated ? <Navigate replace to="/dashboard" /> : <RecoverPassword />}
            />
            <Route
              path={ROUTES.RESET_PASSWORD}
              element={isAuthenticated ? <Navigate replace to="/dashboard" /> : <ResetPassword />}
            />
            
            {/* Ruta para página no encontrada */}
            <Route path="*" element={<PageNotFound />} />
          </Routes>
        </Suspense>
      </Layout>
    </BrowserRouter>
  );
}

export default App;


