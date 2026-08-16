export interface ProjectMemberResponse {
  id: number;
  name: string;
  email: string;
  role: string;
  avatarUrl: string | null;
  joinedAt: string;
}