/** 统一后端响应体 */
export interface ApiResult<T> {
  code: number;
  msg: string;
  data: T;
}

/** 通用分页请求参数 */
export interface PageParams {
  page?: number;
  size?: number;
}

/** 通用分页响应 */
export interface PageResult<T> {
  content: T[];
  totalElements: number;
  totalPages: number;
  number: number;
  size: number;
}
