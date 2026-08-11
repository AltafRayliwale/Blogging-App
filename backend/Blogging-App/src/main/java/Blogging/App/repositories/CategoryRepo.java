package Blogging.App.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import Blogging.App.entities.Category;

public interface CategoryRepo extends JpaRepository<Category, Integer> {
}
