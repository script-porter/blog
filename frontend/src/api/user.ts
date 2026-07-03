import type { UserAccount } from "./auth";
import type { ApiResult } from "./types";
import ApiClient from "./client";

/** 更新个人资料参数 */
export interface UpdateProfileParams {
  username?: string;
}

/** 获取当前登录用户信息 */
export async function getCurrentUser() {
  return await ApiClient.get<UserAccount>("/users/profile");
}

/** 更新个人资料 */
export async function updateProfile(
  params: UpdateProfileParams,
): Promise<UserAccount> {
  const res: ApiResult<UserAccount> = await ApiClient.put<UserAccount>(
    "/users/profile",
    { ...params },
  );
  return res.data;
}
