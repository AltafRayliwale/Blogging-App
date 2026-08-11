package Blogging.App.services.impl;

import Blogging.App.entities.Category;
import Blogging.App.exceptions.ResourceNotFoundException;
import Blogging.App.payloads.CategoryDto;
import Blogging.App.repositories.CategoryRepo;
import Blogging.App.services.CategoryService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;
@Component
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryRepo categoryRepo;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public CategoryDto createCategory(CategoryDto categoryDto) {
        Category cat=this.modelMapper.map(categoryDto,Category.class);
        Category addedCat=this.categoryRepo.save(cat);
        return this.modelMapper.map(addedCat,CategoryDto.class);

    }

    @Override
    public CategoryDto updateCategory(CategoryDto categoryDto, Integer categoryId) throws ResourceNotFoundException {
        Category cat = this.categoryRepo.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "Category Id", categoryId));

        // Setting fields from the DTO to the entity
      //  cat.setCategoryId(categoryDto.getCategoryId());
        cat.setCategoryTitle(categoryDto.getCategoryTitle());
        cat.setCategoryDescription(categoryDto.getCategoryDescription());  // Corrected typo

        // Save the updated category entity
        Category updatedCat = this.categoryRepo.save(cat);

        // Map the updated entity to a DTO and return
        return this.modelMapper.map(updatedCat, CategoryDto.class);
    }

    @Override
    public void deleteCategory(Integer categoryId) throws ResourceNotFoundException {

        this.categoryRepo.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category","Category Id",categoryId));
        this.categoryRepo.deleteById(categoryId);
    }

    @Override
    public CategoryDto getCategory(Integer categoryId) throws ResourceNotFoundException {

        Category cat = this.categoryRepo.findById(categoryId)
                .orElseThrow(()-> new ResourceNotFoundException("Category","category id",categoryId));
        return this.modelMapper.map(cat,CategoryDto.class);

    }

    @Override
    public List<CategoryDto> getCategories() {
     List<Category>  categories=  this.categoryRepo.findAll();
    List<CategoryDto> catDtos= categories.stream().map((cat)-> this.modelMapper.map(cat, CategoryDto.class)).collect(Collectors.toList());
        return catDtos;
    }
}
