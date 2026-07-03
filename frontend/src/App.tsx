import { ConfigProvider } from 'antd';
import zhCN from 'antd/locale/zh_CN';
import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom';
import type { ReactNode } from 'react';
import AuthPage from './pages/Auth';
import MainLayout from './layouts/MainLayout';

/** 登录守卫：无 token 则跳转登录页 */
function AuthGuard({ children }: { children: ReactNode }) {
  const token = localStorage.getItem('token');
  if (!token) {
    return <Navigate to="/auth" replace />;
  }
  return <>{children}</>;
}

function App() {
  return (
    <ConfigProvider
      locale={zhCN}
      theme={{
        token: {
          colorPrimary: '#1677ff',
          borderRadius: 8,
        },
      }}
    >
      <BrowserRouter>
        <Routes>
          <Route path="/" element={<Navigate to="/home" replace />} />
          <Route path="/auth" element={<AuthPage />} />
          <Route
            path="/home"
            element={
              <AuthGuard>
                <MainLayout />
              </AuthGuard>
            }
          />
          <Route path="*" element={<Navigate to="/auth" replace />} />
        </Routes>
      </BrowserRouter>
    </ConfigProvider>
  );
}

export default App;
