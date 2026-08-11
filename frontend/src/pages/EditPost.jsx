import { useEffect, useState } from "react";
import { useParams, useNavigate } from "react-router-dom";
import { toast } from "react-toastify";

import Navbar from "../components/Navbar";

import { getPostById, updatePost } from "../api/postApi";
import { getCategories } from "../api/categoryApi";

function EditPost() {
  const { postId } = useParams();
  const navigate = useNavigate();

  const [title, setTitle] = useState("");
  const [content, setContent] = useState("");

  const [categories, setCategories] = useState([]);
  const [categoryId, setCategoryId] = useState("");

  const [loading, setLoading] = useState(true);
  const [submitting, setSubmitting] = useState(false);

  useEffect(() => {
    loadData();
  }, [postId]);

  async function loadData() {
    try {
      const [postRes, categoryRes] = await Promise.all([
        getPostById(postId),
        getCategories(),
      ]);

      setTitle(postRes.data.title);
      setContent(postRes.data.content);
      setCategoryId(postRes.data.category?.categoryId || "");
      setCategories(categoryRes.data);

    } catch (err) {
      toast.error("Couldn't load this post.");
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
      await updatePost(postId, {
        title,
        content,
      });

      toast.success("Post updated successfully!");

      setTimeout(() => {
        navigate(`/posts/${postId}`);
      }, 1000);

    } catch (err) {
      toast.error("Couldn't save changes.");
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

          <h1>Edit Post</h1>

          <form onSubmit={handleSubmit}>

            <div className="field">
              Title

              <input
                value={title}
                onChange={(e) => setTitle(e.target.value)}
                placeholder="Enter title..."
                required
              />
            </div>

            <div className="field">
              Content

              <textarea
                rows={10}
                value={content}
                onChange={(e) => setContent(e.target.value)}
                placeholder="Write your article..."
                required
              />
            </div>

            <div className="field">
              Category

              <select
                value={categoryId}
                disabled
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

              <span className="hint">
                Category can't be changed after publishing.
              </span>

            </div>

            <button
              className="btn-primary"
              type="submit"
              disabled={submitting}
            >
              {submitting ? "Saving..." : "Save Changes"}
            </button>

          </form>

        </div>

      </div>
    </>
  );
}

export default EditPost;