import { useEffect, useState } from "react";
import Navbar from "../components/Navbar";
import {
  getCategories,
  createCategory,
  updateCategory,
  deleteCategory,
} from "../api/categoryApi";

function Categories() {
  const [categories, setCategories] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");

  // New category form
  const [title, setTitle] = useState("");
  const [description, setDescription] = useState("");
  const [submitting, setSubmitting] = useState(false);

  // Which category (if any) is currently being edited inline
  const [editingId, setEditingId] = useState(null);
  const [editTitle, setEditTitle] = useState("");
  const [editDescription, setEditDescription] = useState("");

  useEffect(() => {
    loadCategories();
  }, []);

  async function loadCategories() {
    try {
      const response = await getCategories();
      setCategories(response.data);
    } catch (err) {
      setError("Couldn't load categories.");
    } finally {
      setLoading(false);
    }
  }

  async function handleCreate(e) {
    e.preventDefault();
    setError("");
    setSubmitting(true);

    try {
      // Matches CategoryDto's validation: title needs 4+ chars,
      // description needs 10+ chars, or the backend will reject it.
      await createCategory({ categoryTitle: title, categoryDescription: description });
      setTitle("");
      setDescription("");
      await loadCategories();
    } catch (err) {
      setError("Couldn't create category. Title needs 4+ characters, description needs 10+.");
    } finally {
      setSubmitting(false);
    }
  }

  function startEditing(cat) {
    setEditingId(cat.categoryId);
    setEditTitle(cat.categoryTitle);
    setEditDescription(cat.categoryDescription);
  }

  async function handleSaveEdit(categoryId) {
    try {
      await updateCategory(categoryId, {
        categoryTitle: editTitle,
        categoryDescription: editDescription,
      });
      setEditingId(null);
      await loadCategories();
    } catch (err) {
      setError("Couldn't save changes.");
    }
  }

  async function handleDelete(categoryId) {
    if (!window.confirm("Delete this category? Posts using it may be affected.")) return;

    try {
      await deleteCategory(categoryId);
      await loadCategories();
    } catch (err) {
      setError("Couldn't delete this category.");
    }
  }

  if (loading) return <p style={{ padding: "2rem" }}>Loading...</p>;

  return (
    <>
      <Navbar />
      <div className="page">
        <h1 style={{ fontFamily: "var(--font-display)" }}>Categories</h1>
        {error && <p className="error-text">{error}</p>}

        {categories.map((cat) => (
          <div className="post-entry" key={cat.categoryId}>
            {editingId === cat.categoryId ? (
              <>
                <div className="field">
                  Title
                  <input value={editTitle} onChange={(e) => setEditTitle(e.target.value)} />
                </div>
                <div className="field">
                  Description
                  <textarea value={editDescription} onChange={(e) => setEditDescription(e.target.value)} rows={2} />
                </div>
                <div style={{ display: "flex", gap: "1rem" }}>
                  <button className="btn-primary" onClick={() => handleSaveEdit(cat.categoryId)}>Save</button>
                  <button className="btn-ghost" onClick={() => setEditingId(null)}>Cancel</button>
                </div>
              </>
            ) : (
              <>
                <h2>{cat.categoryTitle}</h2>
                <p>{cat.categoryDescription}</p>
                <div style={{ display: "flex", gap: "1rem" }}>
                  <button className="btn-ghost" onClick={() => startEditing(cat)}>Edit</button>
                  <button className="btn-ghost" onClick={() => handleDelete(cat.categoryId)}>Delete</button>
                </div>
              </>
            )}
          </div>
        ))}

        <h2 style={{ fontFamily: "var(--font-display)", marginTop: "2rem" }}>New category</h2>
        <form onSubmit={handleCreate} className="form-card">
          <div className="field">
            Title
            <input value={title} onChange={(e) => setTitle(e.target.value)} required />
          </div>
          <div className="field">
            Description
            <textarea value={description} onChange={(e) => setDescription(e.target.value)} required rows={2} />
          </div>
          <button className="btn-primary" type="submit" disabled={submitting}>
            {submitting ? "Creating..." : "Create category"}
          </button>
        </form>
      </div>
    </>
  );
}

export default Categories;