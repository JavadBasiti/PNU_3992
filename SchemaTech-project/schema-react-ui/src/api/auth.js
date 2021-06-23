import client from "./client";

const login = (email, password) => {
  return client.post("/oauth/token", { email, password });
};

export default {
  login,
};
