import {
  FileTextOutlined,
  TeamOutlined,
  SettingOutlined,
  HomeOutlined,
} from '@ant-design/icons';

export default function Dashboard() {
  const username = localStorage.getItem('username') || '未登录';

  return (
    <>
      {/* 欢迎横幅 */}
      <div
        style={{
          background: 'linear-gradient(135deg, #1677ff 0%, #0958d9 100%)',
          borderRadius: 12,
          padding: '28px 32px',
          marginBottom: 24,
          color: '#fff',
        }}
      >
        <h1 style={{ margin: 0, fontSize: 22, fontWeight: 600, letterSpacing: '0.3px' }}>
          你好，{username} 👋
        </h1>
        <p style={{ margin: '8px 0 0', fontSize: 14, opacity: 0.8 }}>
          欢迎回来！当前系统运行正常。
        </p>
      </div>

      {/* 快捷操作卡片 */}
      <div
        style={{
          display: 'grid',
          gridTemplateColumns: 'repeat(auto-fill, minmax(240px, 1fr))',
          gap: 16,
          marginBottom: 24,
        }}
      >
        {[
          { icon: <FileTextOutlined />, title: '内容管理', desc: '管理文章与页面', color: '#1677ff' },
          { icon: <TeamOutlined />,     title: '用户管理', desc: '管理用户与权限', color: '#52c41a' },
          { icon: <SettingOutlined />,  title: '系统设置', desc: '配置系统参数',   color: '#722ed1' },
        ].map((item) => (
          <div
            key={item.title}
            style={{
              background: '#fff',
              borderRadius: 12,
              padding: 24,
              cursor: 'pointer',
              transition: 'all 0.3s',
              border: '1px solid #f0f0f0',
            }}
            className="hover:shadow-lg hover:-translate-y-1"
          >
            <div
              style={{
                width: 44,
                height: 44,
                borderRadius: 10,
                background: `${item.color}12`,
                display: 'flex',
                alignItems: 'center',
                justifyContent: 'center',
                fontSize: 22,
                color: item.color,
                marginBottom: 16,
              }}
            >
              {item.icon}
            </div>
            <h3 style={{ margin: '0 0 4px', fontSize: 16, fontWeight: 600, color: '#333' }}>{item.title}</h3>
            <p style={{ margin: 0, fontSize: 13, color: '#999' }}>{item.desc}</p>
          </div>
        ))}
      </div>

      {/* 主内容卡片占位 */}
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
          <div style={{ fontSize: 48, marginBottom: 16, opacity: 0.15 }}>
            <HomeOutlined />
          </div>
          <p style={{ color: '#bbb', fontSize: 15, margin: 0 }}>选择左侧菜单开始管理</p>
        </div>
      </div>
    </>
  );
}
