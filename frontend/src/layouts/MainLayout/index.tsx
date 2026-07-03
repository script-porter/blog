import { useState, useCallback } from 'react';
import { useNavigate } from 'react-router-dom';
import { Layout } from 'antd';
import Sidebar from './Sidebar';
import HeaderBar from './HeaderBar';
import type { MenuKey } from './Sidebar';
import Dashboard from '../../pages/Home/Dashboard';
import ProfilePage from '../../pages/Profile';

const { Content } = Layout;

/** 占位页面组件 */
function Placeholder({ title }: { title: string }) {
  return (
    <div
      style={{
        background: '#fff',
        borderRadius: 12,
        padding: 32,
        border: '1px solid #f0f0f0',
        minHeight: 240,
        display: 'flex',
        alignItems: 'center',
        justifyContent: 'center',
      }}
    >
      <div style={{ textAlign: 'center' }}>
        <p style={{ color: '#bbb', fontSize: 15, margin: 0 }}>{title} — 开发中</p>
      </div>
    </div>
  );
}

export default function MainLayout() {
  const navigate = useNavigate();
  const [collapsed, setCollapsed] = useState(false);
  const [currentMenu, setCurrentMenu] = useState<MenuKey>('home');

  const handleMenuClick = useCallback((key: MenuKey) => {
    setCurrentMenu(key);
  }, []);

  const handleLogout = useCallback(() => {
    localStorage.removeItem('token');
    localStorage.removeItem('username');
    navigate('/auth', { replace: true });
  }, [navigate]);

  const renderPage = () => {
    switch (currentMenu) {
      case 'home':
        return <Dashboard />;
      case 'profile':
        return <ProfilePage />;
      case 'content':
        return <Placeholder title="内容管理" />;
      case 'users':
        return <Placeholder title="用户管理" />;
      case 'settings':
        return <Placeholder title="系统设置" />;
      default:
        return <Placeholder title="" />;
    }
  };

  return (
    <Layout className="min-h-screen" style={{ background: '#f0f2f5' }}>
      <Sidebar
        collapsed={collapsed}
        currentMenu={currentMenu}
        onMenuClick={handleMenuClick}
      />

      <Layout style={{ marginLeft: collapsed ? 64 : 208, transition: 'margin-left 0.2s' }}>
        <HeaderBar
          collapsed={collapsed}
          currentMenu={currentMenu}
          onToggleCollapse={() => setCollapsed(!collapsed)}
          onMenuClick={handleMenuClick}
          onLogout={handleLogout}
        />

        <Content style={{ margin: 24, minHeight: 'calc(100vh - 56px - 48px)' }}>
          {renderPage()}
        </Content>
      </Layout>
    </Layout>
  );
}
