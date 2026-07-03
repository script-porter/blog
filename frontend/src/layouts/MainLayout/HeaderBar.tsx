import { Avatar, Dropdown } from 'antd';
import {
  MenuFoldOutlined,
  MenuUnfoldOutlined,
  UserOutlined,
  LogoutOutlined,
} from '@ant-design/icons';
import type { MenuProps } from 'antd';

export type MenuKey = 'home' | 'content' | 'users' | 'settings' | 'profile';

const AVATAR_COLORS = [
  '#1677ff', '#52c41a', '#fa8c16', '#eb2f96',
  '#722ed1', '#13c2c2', '#fa541c', '#2f54eb',
];

interface HeaderBarProps {
  collapsed: boolean;
  currentMenu: MenuKey;
  onToggleCollapse: () => void;
  onMenuClick: (key: MenuKey) => void;
  onLogout: () => void;
}

const pageTitle: Record<MenuKey, string> = {
  home:     '首页',
  content:  '内容管理',
  users:    '用户管理',
  settings: '系统设置',
  profile:  '个人中心',
};

export default function HeaderBar({
  collapsed,
  currentMenu,
  onToggleCollapse,
  onMenuClick,
  onLogout,
}: HeaderBarProps) {
  const username = localStorage.getItem('username') || '未登录';
  const avatarChar = username.charAt(0).toUpperCase();
  const avatarColor = AVATAR_COLORS[username.charCodeAt(0) % AVATAR_COLORS.length];

  const dropdownItems: MenuProps['items'] = [
    {
      key: 'userinfo',
      label: (
        <div className="px-1 py-1">
          <div className="font-medium text-gray-800">{username}</div>
          <div className="text-xs text-gray-400">管理员</div>
        </div>
      ),
      disabled: true,
    },
    { type: 'divider' },
    { key: 'profile', icon: <UserOutlined />, label: '个人中心' },
    { type: 'divider' },
    { key: 'logout', icon: <LogoutOutlined />, label: '退出登录', danger: true },
  ];

  const handleDropdown: MenuProps['onClick'] = ({ key }) => {
    if (key === 'profile') onMenuClick('profile');
    if (key === 'logout') onLogout();
  };

  return (
    <div
      style={{
        background: '#fff',
        height: 56,
        padding: '0 24px',
        display: 'flex',
        alignItems: 'center',
        justifyContent: 'space-between',
        borderBottom: '1px solid #f0f0f0',
        boxShadow: '0 1px 4px rgba(0,0,0,0.03)',
        position: 'sticky',
        top: 0,
        zIndex: 99,
      }}
    >
      {/* 左侧：折叠按钮 + 面包屑 */}
      <div className="flex items-center gap-3">
        <button
          onClick={onToggleCollapse}
          style={{
            fontSize: 18,
            color: '#999',
            background: 'none',
            border: 'none',
            cursor: 'pointer',
            padding: '4px 6px',
            borderRadius: 4,
            transition: 'all 0.2s',
            display: 'flex',
            alignItems: 'center',
          }}
          onMouseEnter={(e) => { e.currentTarget.style.color = '#1677ff'; e.currentTarget.style.background = '#f5f5f5'; }}
          onMouseLeave={(e) => { e.currentTarget.style.color = '#999'; e.currentTarget.style.background = 'transparent'; }}
        >
          {collapsed ? <MenuUnfoldOutlined /> : <MenuFoldOutlined />}
        </button>

        <span style={{ color: '#ddd', fontSize: 16, fontWeight: 100 }}>/</span>
        <span style={{ fontSize: 14, color: '#666' }}>管理系统</span>
        <span style={{ color: '#ddd', fontSize: 16, fontWeight: 100 }}>/</span>
        <span style={{ fontSize: 14, fontWeight: 500, color: '#333' }}>
          {pageTitle[currentMenu]}
        </span>
      </div>

      {/* 右侧：用户信息 */}
      <Dropdown
        menu={{ items: dropdownItems, onClick: handleDropdown }}
        placement="bottomRight"
        trigger={['click']}
      >
        <div
          style={{
            display: 'flex',
            alignItems: 'center',
            gap: 10,
            cursor: 'pointer',
            padding: '4px 8px',
            borderRadius: 6,
            transition: 'background 0.2s',
          }}
          onMouseEnter={(e) => { e.currentTarget.style.background = '#f5f5f5'; }}
          onMouseLeave={(e) => { e.currentTarget.style.background = 'transparent'; }}
        >
          <Avatar
            size={32}
            style={{
              backgroundColor: avatarColor,
              fontSize: 14,
              fontWeight: 600,
              cursor: 'pointer',
              flexShrink: 0,
            }}
          >
            {avatarChar}
          </Avatar>
          <span style={{ fontSize: 14, color: '#333', maxWidth: 100 }} className="truncate hidden sm:inline">
            {username}
          </span>
        </div>
      </Dropdown>
    </div>
  );
}
