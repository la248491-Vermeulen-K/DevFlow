export interface LoginResponse {
  token: string;
  refreshToken: string;
  id: number;
  name: string;
  email: string;
}