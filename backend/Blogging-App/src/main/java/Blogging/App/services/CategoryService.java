package Blogging.App.services;

import Blogging.App.exceptions.ResourceNotFoundException;
import Blogging.App.payloads.CategoryDto;
import Blogging.App.repositories.CategoryRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service

public interface CategoryService {

  CategoryDto createCategory(CategoryDto categoryDto);
  CategoryDto updateCategory(CategoryDto categoryDto, Integer categoryId) throws ResourceNotFoundException;
  void deleteCategory(Integer categoryId) throws ResourceNotFoundException;
  CategoryDto getCategory(Integer categoryId) throws ResourceNotFoundException;
  List<CategoryDto> getCategories();
}
