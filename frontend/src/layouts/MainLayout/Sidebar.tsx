import { Menu, theme } from 'antd';
import {
  HomeOutlined,
  AppstoreOutlined,
  FileTextOutlined,
  TeamOutlined,
  SettingOutlined,
} from '@ant-design/icons';
import type { MenuProps } from 'antd';

export type MenuKey = 'home' | 'content' | 'users' | 'settings' | 'profile';

interface SidebarProps {
  collapsed: boolean;
  currentMenu: MenuKey;
  onMenuClick: (key: MenuKey) => void;
}

const menuItems: MenuProps['items'] = [
  { key: 'home', icon: <HomeOutlined />, label: '首页' },
  {
    key: 'management',
    icon: <AppstoreOutlined />,
    label: '业务管理',
    children: [
      { key: 'content', icon: <FileTextOutlined />, label: '内容管理' },
      { key: 'users', icon: <TeamOutlined />, label: '用户管理' },
    ],
  },
  { key: 'settings', icon: <SettingOutlined />, label: '系统设置' },
];

export default function Sidebar({ collapsed, currentMenu, onMenuClick }: SidebarProps) {
  const { token: themeToken } = theme.useToken();

  return (
    <div
      style={{
        width: collapsed ? 64 : 208,
        height: '100vh',
        background: '#001529',
        position: 'fixed',
        left: 0,
        top: 0,
        zIndex: 100,
        display: 'flex',
        flexDirection: 'column',
        transition: 'width 0.2s',
        overflow: 'hidden',
      }}
    >
      {/* Logo */}
      <div
        style={{
          height: 64,
          display: 'flex',
          alignItems: 'center',
          justifyContent: 'center',
          borderBottom: '1px solid rgba(255,255,255,0.08)',
          flexShrink: 0,
        }}
      >
        <div
          style={{
            width: 32,
            height: 32,
            borderRadius: 6,
            background: themeToken.colorPrimary,
            display: 'flex',
            alignItems: 'center',
            justifyContent: 'center',
            color: '#fff',
            fontWeight: 700,
            fontSize: 18,
            flexShrink: 0,
          }}
        >
          M
        </div>
        {!collapsed && (
          <span
            style={{
              marginLeft: 10,
              fontSize: 18,
              fontWeight: 600,
              color: '#fff',
              whiteSpace: 'nowrap',
              letterSpacing: '0.5px',
            }}
          >
            管理系统
          </span>
        )}
      </div>

      {/* 导航菜单 */}
      <div style={{ flex: 1, overflow: 'auto' }}>
        <Menu
          theme="dark"
          mode="inline"
          inlineCollapsed={collapsed}
          selectedKeys={[currentMenu]}
          defaultSelectedKeys={['home']}
          items={menuItems}
          onClick={({ key }) => onMenuClick(key as MenuKey)}
          style={{ background: 'transparent', borderInlineEnd: 'none', marginTop: 4 }}
        />
      </div>
    </div>
  );
}
