package Blogging.App.Controller;

import Blogging.App.entities.Category;
import Blogging.App.exceptions.ResourceNotFoundException;
import Blogging.App.payloads.ApiResponse;
import Blogging.App.payloads.CategoryDto;
import Blogging.App.services.CategoryService;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/categories")
public class CategoryController {

    @Autowired
    private final CategoryService categoryService;

//    @Autowired
//    public final ModelMapper modelMapper;
    public CategoryController(CategoryService categoryService, ModelMapper modelMapper) {
        this.categoryService = categoryService;
//        this.modelMapper = modelMapper;
    }

    @PostMapping("/")
    public ResponseEntity<CategoryDto> createCategory(@Valid @RequestBody CategoryDto categoryDto) {
        CategoryDto category = this.categoryService.createCategory(categoryDto);
        return new ResponseEntity<CategoryDto>(category , HttpStatus.CREATED);

    }

    @PutMapping("/{categoryId}")
    public ResponseEntity<CategoryDto> updateCategory(@RequestBody CategoryDto categoryDto , @PathVariable Integer categoryId ) throws ResourceNotFoundException {
        CategoryDto category=this.categoryService.updateCategory(categoryDto,categoryId);
        return new ResponseEntity<CategoryDto>(category,HttpStatus.OK);
    }

    @DeleteMapping
     ResponseEntity<ApiResponse> deleteCategory(@Valid @PathVariable Integer categoryId) throws ResourceNotFoundException {
        this.categoryService.deleteCategory(categoryId);
        return new ResponseEntity<ApiResponse>(new ApiResponse("category is deleted successfully !!",true),HttpStatus.OK);
    }

    @GetMapping("/{categoryId}")
    public ResponseEntity<CategoryDto> getCategory(@PathVariable Integer categoryId) throws ResourceNotFoundException {
        CategoryDto category = this.categoryService.getCategory(categoryId);
        return new ResponseEntity<CategoryDto>(category,HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<CategoryDto>> getCategories() {
       List<CategoryDto>  category = this.categoryService.getCategories();
        return ResponseEntity.ok(category);
    }



}
