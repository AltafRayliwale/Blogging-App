import { useState } from "react";
import { useNavigate, Link } from "react-router-dom";
import { toast } from "react-toastify";
import { registerUser } from "../api/authApi";

function Register() {
  const [form, setForm] = useState({
    username: "",
    email: "",
    password: "",
    about: "",
  });

  const [submitting, setSubmitting] = useState(false);

  const navigate = useNavigate();

  function handleChange(e) {
    setForm({
      ...form,
      [e.target.name]: e.target.value,
    });
  }

  async function handleSubmit(e) {
    e.preventDefault();

    setSubmitting(true);

    try {
      await registerUser(form);

      toast.success("Account created successfully!");

      setTimeout(() => {
        navigate("/login");
      }, 1200);

    } catch (err) {
      const message =
        err.response?.data?.message ||
        "Registration failed. Please check your details.";

      toast.error(message);
    } finally {
      setSubmitting(false);
    }
  }

  return (
    <div className="auth-page">
      <div className="form-card">

        <h1>Create Account</h1>

        <form onSubmit={handleSubmit}>

          <div className="field">
            Username

            <input
              name="username"
              value={form.username}
              onChange={handleChange}
              placeholder="Enter your name"
              required
            />
          </div>

          <div className="field">
            Email

            <input
              name="email"
              type="email"
              value={form.email}
              onChange={handleChange}
              placeholder="Enter your email"
              required
            />
          </div>

          <div className="field">
            Password

            <input
              name="password"
              type="password"
              value={form.password}
              onChange={handleChange}
              placeholder="Enter password"
              required
              maxLength={10}
            />

            <span className="hint">
              Password must be 3–10 characters.
            </span>
          </div>

          <div className="field">
            About You

            <textarea
              name="about"
              value={form.about}
              onChange={handleChange}
              rows={3}
              placeholder="Tell us something about yourself..."
              required
            />
          </div>

          <button
            className="btn-primary"
            type="submit"
            disabled={submitting}
          >
            {submitting ? "Creating Account..." : "Create Account"}
          </button>

        </form>

        <p className="form-footer">
          Already have an account?{" "}
          <Link to="/login">
            Log in
          </Link>
        </p>

      </div>
    </div>
  );
}

export default Register;