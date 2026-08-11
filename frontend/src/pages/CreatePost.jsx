import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import { toast } from "react-toastify";

import Navbar from "../components/Navbar";

import { getAllUsers } from "../api/userApi";
import { getCategories } from "../api/categoryApi";
import { createPost } from "../api/postApi";
import { decodeToken } from "../utils/jwt";

function CreatePost() {
  const [title, setTitle] = useState("");
  const [content, setContent] = useState("");
  const [categories, setCategories] = useState([]);
  const [categoryId, setCategoryId] = useState("");
  const [currentUserId, setCurrentUserId] = useState(null);

  const [loading, setLoading] = useState(true);
  const [submitting, setSubmitting] = useState(false);

  const navigate = useNavigate();

  useEffect(() => {
    loadInitialData();
  }, []);

  async function loadInitialData() {
    try {
      const token = localStorage.getItem("token");
      const decoded = decodeToken(token);

      const myEmail = decoded?.sub;

      const usersResponse = await getAllUsers();

      const me = usersResponse.data.find(
        (user) => user.email === myEmail
      );

      if (!me) {
        toast.error("Please login again.");
        navigate("/login");
        return;
      }

      setCurrentUserId(me.id);

      const categoriesResponse = await getCategories();

      setCategories(categoriesResponse.data);

      if (categoriesResponse.data.length > 0) {
        setCategoryId(categoriesResponse.data[0].categoryId);
      }

    } catch (err) {
      toast.error("Couldn't load page.");
    } finally {
      setLoading(false);
    }
  }

  async function handleSubmit(e) {
    e.preventDefault();

    if (!title.trim()) {
      toast.warning("Title is required.");
      return;
    }

    if (!content.trim()) {
      toast.warning("Content is required.");
      return;
    }

    setSubmitting(true);

    try {
      const response = await createPost(
        currentUserId,
        categoryId,
        {
          title,
          content,
        }
      );

      toast.success("Post published successfully!");

      setTimeout(() => {
        navigate(`/posts/${response.data.postId}`);
      }, 1000);

    } catch (err) {
      toast.error("Couldn't publish post.");
    } finally {
      setSubmitting(false);
    }
  }

  if (loading) {
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

          <h1>Write a New Post</h1>

          <form onSubmit={handleSubmit}>

            <div className="field">
              Title

              <input
                value={title}
                onChange={(e) => setTitle(e.target.value)}
                placeholder="Enter post title..."
                required
              />
            </div>

            <div className="field">
              Content

              <textarea
                rows={10}
                value={content}
                onChange={(e) => setContent(e.target.value)}
                placeholder="Write your article here..."
                required
              />
            </div>

            <div className="field">
              Category

              <select
                value={categoryId}
                onChange={(e) => setCategoryId(e.target.value)}
              >
                {categories.map((cat) => (
                  <option
                    key={cat.categoryId}
                    value={cat.categoryId}
                  >
                    {cat.categoryTitle}
                  </option>
                ))}
              </select>

            </div>

            <button
              className="btn-primary"
              type="submit"
              disabled={submitting}
            >
              {submitting
                ? "Publishing..."
                : "Publish Post"}
            </button>

          </form>

        </div>

      </div>
    </>
  );
}

export default CreatePost;