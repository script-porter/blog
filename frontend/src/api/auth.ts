import ApiClient from "./client";

// ===== 参数类型 =====

export interface RegisterParams {
  phone: string;
  password: string;
}

export interface LoginParams {
  phone: string;
  password: string;
}

// ===== 响应类型 =====

export interface LoginResult {
  token: string;
  username: string;
}

/** 用户账户信息（对应后端 User 实体） */
export interface UserAccount {
  id: number;
  username: string;
  phone: string;
  status: number;
  createdAt: string;
  updatedAt: string;
}

/** 用户扩展资料（对应后端 UserInfo 实体） */
export interface UserInfo {
  userId: number;
  signature?: string;
  avatar?: string;
  occupation?: string;
  address?: string;
  birthday?: string;
  identify?: string;
  createdAt: string;
  updatedAt: string;
}

// ===== API 接口 =====

/** 注册 */
export async function register(params: RegisterParams) {
  return await ApiClient.post("/users", params);
}

/** 登录 */
export async function login(params: LoginParams) {
  return await ApiClient.post<LoginResult>("/users/login", params);
}
