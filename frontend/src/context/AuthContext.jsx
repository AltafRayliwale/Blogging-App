import { createContext, useContext, useState, useEffect } from "react";
import axiosClient from "../api/axiosClient";
import { getAllUsers } from "../api/userApi";
import { decodeToken } from "../utils/jwt";

const AuthContext = createContext(null);

export function AuthProvider({ children }) {
  const [token, setToken] = useState(localStorage.getItem("token"));
  const [currentUser, setCurrentUser] = useState(null);

  const isLoggedIn = !!token;

  async function refreshCurrentUser() {
    const savedToken = localStorage.getItem("token");
    if (!savedToken) {
      setCurrentUser(null);
      return;
    }

    const myEmail = decodeToken(savedToken)?.sub;
    try {
      const res = await getAllUsers();
      const me = res.data.find((u) => u.email === myEmail);
      console.log("Current User:", me);

      setCurrentUser(me || null);
    } catch (err) {
      setCurrentUser(null);
    }
  }

  useEffect(() => {
    refreshCurrentUser();
  }, [token]);

  async function login(username, password) {
    const response = await axiosClient.post("/api/v1/auth/login", {
      username,
      password,
    });

    const newToken = response.data.token;
    localStorage.setItem("token", newToken);
    setToken(newToken);
  }

  function logout() {
    localStorage.removeItem("token");
    setToken(null);
  }

  return (
    <AuthContext.Provider value={{ token, isLoggedIn, currentUser, login, logout, refreshCurrentUser }}>
      {children}
    </AuthContext.Provider>
  );
}

export function useAuth() {
  return useContext(AuthContext);
}