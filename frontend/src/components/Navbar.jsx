import { useEffect, useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import { useAuth } from "../context/AuthContext";
import { getCategories } from "../api/categoryApi";
import { getProfileImageUrl } from "../api/userApi";

function Navbar() {
  const { logout, currentUser } = useAuth();

  const [showLogout, setShowLogout] = useState(false);
  const [showCategories, setShowCategories] = useState(false);
  const [categories, setCategories] = useState([]);

  const navigate = useNavigate();

  useEffect(() => {
    getCategories()
      .then((res) => setCategories(res.data))
      .catch(() => {});
  }, []);

  function goToCategory(categoryId) {
    setShowCategories(false);
    navigate(`/?category=${categoryId}`);
  }

  return (
    <div className="navbar-wrap">
      <div className="navbar">

        {/* Left Side */}
        <div className="navbar-left">
          <Link to="/" className="navbar-brand">
            Blogging App
          </Link>
        </div>

        {/* Right Side */}
        <div
          className="navbar-actions"
          style={{
            position: "relative",
            display: "flex",
            alignItems: "center",
            gap: "28px",
          }}
        >

          {/* Categories */}
          <div style={{ position: "relative" }}>
            <span
              className="btn-ghost"
              style={{ cursor: "pointer" }}
              onClick={() => setShowCategories(!showCategories)}
            >
              Categories
            </span>

            {showCategories && (
              <div className="dropdown-panel">
                {categories.map((cat) => (
                  <div
                    key={cat.categoryId}
                    onClick={() => goToCategory(cat.categoryId)}
                    style={{
                      cursor: "pointer",
                      padding: "10px 14px",
                    }}
                  >
                    {cat.categoryTitle}
                  </div>
                ))}

                <Link
                  to="/categories"
                  onClick={() => setShowCategories(false)}
                  className="dropdown-add"
                >
                  + Add Category
                </Link>
              </div>
            )}
          </div>

          {/* Write */}
          <Link to="/create-post" className="btn-ghost">
            Write
          </Link>

          {/* Profile */}
          <div style={{ position: "relative" }}>
            <div
              className="btn-ghost"
              onClick={() => setShowLogout(!showLogout)}
              style={{
                cursor: "pointer",
                display: "flex",
                alignItems: "center",
                gap: "10px",
              }}
            >
              <img
                src={
                  currentUser?.profileImage
                    ? getProfileImageUrl(currentUser.profileImage)
                    : "https://ui-avatars.com/api/?name=" +
                      encodeURIComponent(currentUser?.username || "User")
                }
                alt="Profile"
                style={{
                  width: "42px",
                  height: "42px",
                  borderRadius: "50%",
                  objectFit: "cover",
                }}
              />

              <span>{currentUser?.username || "Account"}</span>
            </div>

            {showLogout && (
              <div className="dropdown-panel">
                <Link
    to="/profile"
    onClick={() => setShowLogout(false)}
    className="dropdown-add"
>
    Profile
</Link>

<Link
    to="/my-posts"
    onClick={() => setShowLogout(false)}
    className="dropdown-add"
>
    My Posts
</Link>

<button
    className="btn-ghost dropdown-add"
    onClick={logout}
>
    Logout
</button>

                
              </div>
            )}
          </div>

        </div>

      </div>
    </div>
  );
}

export default Navbar;