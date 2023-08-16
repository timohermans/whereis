export type User = {
  token: string;
  refreshToken: string;
  email: string;
  roles: string[];
  tokenType: string;
};

export const userKey = "user";
