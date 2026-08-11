import { useEffect, useState } from "react";
import { toast } from "react-toastify";

import Navbar from "../components/Navbar";
import { useAuth } from "../context/AuthContext";
import {
  updateUser,
  uploadProfileImage,
  getProfileImageUrl,
} from "../api/userApi";

function Profile() {
  const { currentUser, refreshCurrentUser } = useAuth();

  const [username, setUsername] = useState("");
  const [about, setAbout] = useState("");
  const [password, setPassword] = useState("");

  const [submitting, setSubmitting] = useState(false);
  const [uploading, setUploading] = useState(false);
  const [selectedFile, setSelectedFile] = useState(null);

  useEffect(() => {
    if (currentUser) {
      setUsername(currentUser.username || "");
      setAbout(currentUser.about || "");
    }
  }, [currentUser]);

  async function handleProfileImageUpload() {
    if (!selectedFile) {
      toast.warning("Please select an image first.");
      return;
    }

    setUploading(true);

    try {
      await uploadProfileImage(currentUser.id, selectedFile);

      await refreshCurrentUser();

      toast.success("Profile picture updated successfully!");

      setSelectedFile(null);
    } catch (err) {
      toast.error("Couldn't upload profile picture.");
    } finally {
      setUploading(false);
    }
  }

  async function handleSubmit(e) {
    e.preventDefault();

    if (!username.trim()) {
      toast.warning("Display name is required.");
      return;
    }

    if (!about.trim()) {
      toast.warning("About section is required.");
      return;
    }

    if (!password.trim()) {
      toast.warning("Please enter your password.");
      return;
    }

    setSubmitting(true);

    try {
      await updateUser(currentUser.id, {
        username,
        about,
        email: currentUser.email,
        password,
      });

      await refreshCurrentUser();

      setPassword("");

      toast.success("Profile updated successfully!");

    } catch (err) {
      toast.error("Couldn't update profile. Please check your password.");
    } finally {
      setSubmitting(false);
    }
  }

  if (!currentUser) {
    return (
      <>
        <Navbar />
        <div className="page">
          <h2>Loading...</h2>
        </div>
      </>
    );
  }

  return (
    <>
      <Navbar />

      <div className="page">

        <div className="form-card">

          <h1>Edit Profile</h1>

          <div
            style={{
              textAlign: "center",
              marginBottom: "1.8rem",
            }}
          >
            <img
              src={
                currentUser.profileImage
                  ? getProfileImageUrl(currentUser.profileImage)
                  : "https://ui-avatars.com/api/?name=" +
                    encodeURIComponent(username)
              }
              alt="Profile"
              style={{
                width: "120px",
                height: "120px",
                borderRadius: "50%",
                objectFit: "cover",
                border: "3px solid #ddd",
              }}
            />

            <br />
            <br />

            <input
              type="file"
              accept="image/*"
              onChange={(e) => setSelectedFile(e.target.files[0])}
            />

            <br />
            <br />

            <button
              type="button"
              className="btn-secondary"
              onClick={handleProfileImageUpload}
              disabled={uploading}
            >
              {uploading
                ? "Uploading..."
                : "Upload Profile Picture"}
            </button>
          </div>

          <form onSubmit={handleSubmit}>

            <div className="field">
              Display Name

              <input
                value={username}
                onChange={(e) => setUsername(e.target.value)}
                required
              />
            </div>

            <div className="field">
              About You

              <textarea
                rows={4}
                value={about}
                onChange={(e) => setAbout(e.target.value)}
                required
              />
            </div>

            <div className="field">
              Confirm Password

              <input
                type="password"
                value={password}
                onChange={(e) => setPassword(e.target.value)}
                maxLength={10}
                required
              />

              <span className="hint">
                Enter your password to save changes.
              </span>
            </div>

            <button
              className="btn-primary"
              type="submit"
              disabled={submitting}
            >
              {submitting
                ? "Saving..."
                : "Save Changes"}
            </button>

          </form>

        </div>

      </div>
    </>
  );
}

export default Profile;