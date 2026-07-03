import { useState } from "react";
import { useNavigate } from "react-router-dom";
import { Tabs, Form, Input, Button, message } from "antd";
import { PhoneOutlined, LockOutlined } from "@ant-design/icons";
import { register, login } from "../../api/auth";
import type { RegisterParams, LoginParams } from "../../api/auth";

type TabKey = "login" | "register";

export default function AuthPage() {
  const navigate = useNavigate();
  const [activeTab, setActiveTab] = useState<TabKey>("login");
  const [loading, setLoading] = useState(false);
  const [loginForm] = Form.useForm<LoginParams>();
  const [registerForm] = Form.useForm<RegisterParams>();

  const handleLogin = async (values: LoginParams) => {
    setLoading(true);
    try {
      const { data } = await login(values);
      const { token, username } = data;
      localStorage.setItem("token", token);
      localStorage.setItem("username", username);
      message.success(`欢迎回来，${username || values.phone}`);
      navigate("/home", { replace: true });
    } catch (err: unknown) {
      const error = err as Error;
      message.error(error.message || "登录失败");
    } finally {
      setLoading(false);
    }
  };

  const handleRegister = async (values: RegisterParams) => {
    setLoading(true);
    try {
      await register(values);
      message.success("注册成功，请登录");
      setActiveTab("login");
      loginForm.setFieldsValue({ phone: values.phone });
    } catch (err: unknown) {
      const error = err as Error;
      message.error(error.message || "注册失败");
    } finally {
      setLoading(false);
    }
  };

  const tabItems = [
    {
      key: "login",
      label: <span className="text-base font-medium">登录</span>,
      children: (
        <Form
          form={loginForm}
          layout="vertical"
          onFinish={handleLogin}
          autoComplete="off"
          className="mt-6"
          size="large"
        >
          <Form.Item
            name="phone"
            label="手机号"
            rules={[
              { required: true, message: "请输入手机号" },
              { pattern: /^1\d{10}$/, message: "手机号格式不正确" },
            ]}
          >
            <Input
              prefix={<PhoneOutlined />}
              placeholder="请输入手机号"
              maxLength={11}
            />
          </Form.Item>

          <Form.Item
            name="password"
            label="密码"
            rules={[
              { required: true, message: "请输入密码" },
              { min: 6, message: "密码至少6位" },
            ]}
          >
            <Input.Password
              prefix={<LockOutlined />}
              placeholder="请输入密码"
            />
          </Form.Item>

          <Form.Item className="mb-0">
            <Button
              type="primary"
              htmlType="submit"
              loading={loading}
              block
              className="h-11 text-base font-medium rounded-lg"
            >
              登 录
            </Button>
          </Form.Item>
        </Form>
      ),
    },
    {
      key: "register",
      label: <span className="text-base font-medium">注册</span>,
      children: (
        <Form
          form={registerForm}
          layout="vertical"
          onFinish={handleRegister}
          autoComplete="off"
          className="mt-6"
          size="large"
        >
          <Form.Item
            name="phone"
            label="手机号"
            rules={[
              { required: true, message: "请输入手机号" },
              { pattern: /^1\d{10}$/, message: "手机号格式不正确" },
            ]}
          >
            <Input
              prefix={<PhoneOutlined />}
              placeholder="请输入手机号"
              maxLength={11}
            />
          </Form.Item>

          <Form.Item
            name="password"
            label="密码"
            rules={[
              { required: true, message: "请输入密码" },
              { min: 6, message: "密码至少6位" },
              { max: 20, message: "密码最多20位" },
            ]}
          >
            <Input.Password
              prefix={<LockOutlined />}
              placeholder="请输入密码"
            />
          </Form.Item>

          <Form.Item
            name="confirmPassword"
            label="确认密码"
            dependencies={["password"]}
            rules={[
              { required: true, message: "请再次输入密码" },
              ({ getFieldValue }) => ({
                validator(_, value) {
                  if (!value || getFieldValue("password") === value) {
                    return Promise.resolve();
                  }
                  return Promise.reject(new Error("两次输入的密码不一致"));
                },
              }),
            ]}
          >
            <Input.Password
              prefix={<LockOutlined />}
              placeholder="请再次输入密码"
            />
          </Form.Item>

          <Form.Item className="mb-0">
            <Button
              type="primary"
              htmlType="submit"
              loading={loading}
              block
              className="h-11 text-base font-medium rounded-lg"
            >
              注 册
            </Button>
          </Form.Item>
        </Form>
      ),
    },
  ];

  return (
    <div className="min-h-screen flex items-center justify-center bg-gradient-to-br from-slate-50 to-blue-50 dark:from-gray-900 dark:to-gray-800 px-4">
      <div className="w-full max-w-md">
        {/* 顶部标题 */}
        <div className="text-center mb-8">
          <h1 className="text-3xl font-bold text-gray-800 dark:text-gray-100 tracking-tight">
            博客系统
          </h1>
          <p className="mt-2 text-gray-500 dark:text-gray-400">
            记录生活，分享思考
          </p>
        </div>

        {/* 登录/注册卡片 */}
        <div className="bg-white dark:bg-gray-800 rounded-2xl shadow-lg border border-gray-100 dark:border-gray-700 p-8">
          <Tabs
            activeKey={activeTab}
            onChange={(key) => setActiveTab(key as TabKey)}
            centered
            items={tabItems}
            className="auth-tabs"
          />
        </div>

        {/* 底部版权 */}
        <p className="text-center mt-6 text-xs text-gray-400 dark:text-gray-500">
          &copy; {new Date().getFullYear()} Blog System. All rights reserved.
        </p>
      </div>
    </div>
  );
}
