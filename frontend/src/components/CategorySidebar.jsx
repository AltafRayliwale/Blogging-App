import { useEffect, useState } from "react";
import { getCategories } from "../api/categoryApi";

function CategorySidebar({ activeCategoryId, onSelectCategory }) {
  const [categories, setCategories] = useState([]);

  useEffect(() => {
    getCategories()
      .then((res) => setCategories(res.data))
      .catch(() => {});
  }, []);

  return (
    <div className="sidebar">
      <h3>Categories</h3>

      <a
        className={!activeCategoryId ? "active" : ""}
        onClick={() => onSelectCategory(null)}
        style={{ cursor: "pointer" }}
      >
        All Posts
      </a>

      {categories.map((cat) => (
        <a
          key={cat.categoryId}
          className={activeCategoryId === cat.categoryId ? "active" : ""}
          onClick={() => onSelectCategory(cat.categoryId)}
          style={{ cursor: "pointer" }}
        >
          {cat.categoryTitle}
        </a>
      ))}
    </div>
  );
}

export default CategorySidebar;