import { useEffect, useState } from "react";
import {
  Descriptions,
  Badge,
  Skeleton,
  Card,
  Tag,
  Avatar,
  Button,
  Form,
  Input,
  message,
  Space,
} from "antd";
import {
  UserOutlined,
  ClockCircleOutlined,
  PhoneOutlined,
  EditOutlined,
  CloseOutlined,
  CheckOutlined,
} from "@ant-design/icons";
import { getCurrentUser, updateProfile } from "../../api/user";
import type { UpdateProfileParams } from "../../api/user";
import type { UserAccount } from "../../api/auth";

const AVATAR_COLORS = [
  "#1677ff",
  "#52c41a",
  "#fa8c16",
  "#eb2f96",
  "#722ed1",
  "#13c2c2",
  "#fa541c",
  "#2f54eb",
];

export default function ProfilePage() {
  const [user, setUser] = useState<UserAccount | null>(null);
  const [loading, setLoading] = useState(true);
  const [editing, setEditing] = useState(false);
  const [saving, setSaving] = useState(false);
  const [form] = Form.useForm<UpdateProfileParams>();

  const fetchUser = () => {
    setLoading(true);
    getCurrentUser()
      .then(({ data }) => setUser(data))
      .catch(() => {
        const username = localStorage.getItem("username");
        if (username) {
          setUser({
            id: 0,
            username,
            phone: username,
            status: 1,
            createdAt: "",
            updatedAt: "",
          });
        }
      })
      .finally(() => setLoading(false));
  };

  useEffect(() => {
    (() => {
      fetchUser();
    })();
  }, []);

  const handleEdit = () => {
    form.setFieldsValue({
      username: user?.username || "",
    });
    setEditing(true);
  };

  const handleCancel = () => {
    setEditing(false);
    form.resetFields();
  };

  const handleSave = async () => {
    try {
      const values = await form.validateFields();
      setSaving(true);
      const updated = await updateProfile(values);
      setUser(updated);
      localStorage.setItem("username", updated.username || updated.phone);
      message.success("个人资料更新成功");
      setEditing(false);
    } catch (err: unknown) {
      if (err instanceof Error) {
        message.error(err.message);
      }
    } finally {
      setSaving(false);
    }
  };

  if (loading) {
    return (
      <Card style={{ borderRadius: 12 }}>
        <Skeleton active avatar paragraph={{ rows: 4 }} />
      </Card>
    );
  }

  if (!user) {
    return (
      <Card style={{ borderRadius: 12 }}>
        <div style={{ textAlign: "center", padding: 48, color: "#999" }}>
          无法获取用户信息
        </div>
      </Card>
    );
  }

  const avatarColor =
    AVATAR_COLORS[user.phone?.charCodeAt(0) % AVATAR_COLORS.length];

  return (
    <div>
      {/* 用户头像与基本信息 */}
      <Card style={{ borderRadius: 12, marginBottom: 24 }}>
        <div style={{ display: "flex", alignItems: "center", gap: 24 }}>
          <Avatar
            size={80}
            style={{
              backgroundColor: avatarColor,
              fontSize: 32,
              fontWeight: 600,
              flexShrink: 0,
              boxShadow: `0 4px 14px ${avatarColor}44`,
            }}
          >
            {(user.username || user.phone).charAt(0).toUpperCase()}
          </Avatar>
          <div style={{ flex: 1 }}>
            <h2
              style={{
                margin: "0 0 4px",
                fontSize: 22,
                fontWeight: 600,
                color: "#1a1a1a",
              }}
            >
              {user.username || "未设置昵称"}
            </h2>
            <p style={{ margin: 0, color: "#999", fontSize: 14 }}>
              <PhoneOutlined style={{ marginRight: 6 }} />
              {user.phone}
            </p>
          </div>
          <div style={{ textAlign: "right" }}>
            <div style={{ marginBottom: 8 }}>
              <Badge
                status={user.status === 1 ? "success" : "error"}
                text={
                  <span style={{ color: "#666", fontSize: 14 }}>
                    {user.status === 1 ? "正常" : "已禁用"}
                  </span>
                }
              />
            </div>
            {!editing && (
              <Button
                type="primary"
                icon={<EditOutlined />}
                onClick={handleEdit}
                size="small"
              >
                编辑资料
              </Button>
            )}
          </div>
        </div>
      </Card>

      {/* 详细信息 / 编辑表单 */}
      <Card
        title={
          <Space>
            <UserOutlined />
            <span style={{ fontSize: 16, fontWeight: 600 }}>
              {editing ? "编辑个人资料" : "详细信息"}
            </span>
          </Space>
        }
        style={{ borderRadius: 12 }}
        extra={
          editing ? (
            <Space>
              <Button
                icon={<CloseOutlined />}
                onClick={handleCancel}
                size="small"
              >
                取消
              </Button>
              <Button
                type="primary"
                icon={<CheckOutlined />}
                loading={saving}
                onClick={handleSave}
                size="small"
              >
                保存
              </Button>
            </Space>
          ) : null
        }
      >
        {editing ? (
          <Form form={form} layout="vertical" style={{ maxWidth: 480 }}>
            <Form.Item
              label="昵称"
              name="username"
              rules={[{ max: 50, message: "昵称不超过50个字符" }]}
            >
              <Input
                prefix={<UserOutlined style={{ color: "#bbb" }} />}
                placeholder="请输入昵称"
              />
            </Form.Item>
          </Form>
        ) : (
          <Descriptions
            column={{ xs: 1, sm: 2 }}
            styles={{
              content: { color: "#333" },
              label: { color: "#666", width: 100 },
            }}
          >
            <Descriptions.Item
              label={
                <span>
                  <PhoneOutlined style={{ marginRight: 6 }} />
                  手机号
                </span>
              }
            >
              {user.phone || "-"}
            </Descriptions.Item>
            <Descriptions.Item
              label={
                <span>
                  <UserOutlined style={{ marginRight: 6 }} />
                  昵称
                </span>
              }
            >
              {user.username || <span style={{ color: "#bbb" }}>未设置</span>}
            </Descriptions.Item>
            <Descriptions.Item label="状态">
              <Tag color={user.status === 1 ? "green" : "red"}>
                {user.status === 1 ? "正常" : "已禁用"}
              </Tag>
            </Descriptions.Item>
            <Descriptions.Item
              label={
                <span>
                  <ClockCircleOutlined style={{ marginRight: 6 }} />
                  创建时间
                </span>
              }
            >
              {user.createdAt || "-"}
            </Descriptions.Item>
            <Descriptions.Item
              label={
                <span>
                  <ClockCircleOutlined style={{ marginRight: 6 }} />
                  更新时间
                </span>
              }
            >
              {user.updatedAt || "-"}
            </Descriptions.Item>
          </Descriptions>
        )}
      </Card>
    </div>
  );
}
