import { useState } from "react";
import { useAuth } from "../context/AuthContext";
import { useNavigate, Link } from "react-router-dom";
import { toast } from "react-toastify";

function Login() {
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const [loading, setLoading] = useState(false);

  const { login } = useAuth();
  const navigate = useNavigate();

  async function handleSubmit(e) {
    e.preventDefault();

    setLoading(true);

    try {
      await login(username, password);

      toast.success("Welcome back!");

      navigate("/");
    } catch (err) {
      toast.error("Invalid username or password.");
    } finally {
      setLoading(false);
    }
  }

  return (
    <div className="auth-page">
      <div className="form-card">

        <h1>Log in</h1>

        <form onSubmit={handleSubmit}>

          <div className="field">
            Username or Email

            <input
              type="text"
              value={username}
              onChange={(e) => setUsername(e.target.value)}
              placeholder="Enter email"
              required
            />
          </div>

          <div className="field">
            Password

            <input
              type="password"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              placeholder="Enter password"
              required
            />
          </div>

          <button
            className="btn-primary"
            type="submit"
            disabled={loading}
          >
            {loading ? "Logging in..." : "Log in"}
          </button>

        </form>

        <p className="form-footer">
          Don't have an account?{" "}
          <Link to="/register">
            Sign up
          </Link>
        </p>

      </div>
    </div>
  );
}

export default Login;