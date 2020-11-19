package com.codegym.controller;

import com.codegym.model.Blog;
import com.codegym.service.BlogService;
import com.codegym.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class ApiBlogController {
    @Autowired
    private BlogService blogService;
    @Autowired
    private CategoryService categoryService;

    @RequestMapping(value = "/api/blogs", method = RequestMethod.GET)
    public ResponseEntity<Page<Blog>> listAllCustomers(@RequestParam(value = "page",required = false,defaultValue = "1") int page,
                                                       @RequestParam(value = "search", required = false,defaultValue = "")String search) {

        Page<Blog> blogs = blogService.findAllByNameContaining(search,PageRequest.of(page-1,5));
        if (blogs.isEmpty()) {
            return new ResponseEntity<Page<Blog>>(HttpStatus.NO_CONTENT);//You many decide to return HttpStatus.NOT_FOUND
        }
        return new ResponseEntity<Page<Blog>>(blogs, HttpStatus.OK);
    }

    @DeleteMapping(value = "/api/blogs/{id}")
    public ResponseEntity<Blog> delete(@PathVariable("id") long id) {
        Blog blog = blogService.findOne(id);
        if (blog == null) {
            System.out.println("Blog with id " + id + " not found");
            return new ResponseEntity<Blog>(HttpStatus.NOT_FOUND);
        }
        blogService.remove(id);
        return new ResponseEntity<Blog>(blog, HttpStatus.OK);
    }


    @RequestMapping(value = "/api/blogs/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Blog> getBlogById(@PathVariable("id") long id) {
        Blog blog = blogService.findOne(id);
        if (blog == null) {
            return new ResponseEntity<Blog>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<Blog>(blog, HttpStatus.OK);
    }

    @PutMapping(value = "/api/blogs/")
    public ResponseEntity<Blog> getBlogById(@RequestBody Blog blogUpdate) {
        if (blogUpdate == null) {
            return new ResponseEntity<Blog>(HttpStatus.NOT_FOUND);
        }
        blogService.save(blogUpdate);
        return new ResponseEntity<Blog>(blogUpdate, HttpStatus.OK);
    }

    @RequestMapping(value = "/api/blogs/search", method = RequestMethod.GET)
    public ResponseEntity<Page<Blog>> listBlogsByName(@RequestParam("name") String name, Pageable pageable) {
        Page<Blog> blogs = blogService.findAllByNameContaining(name,pageable);
        if (blogs.isEmpty()) {
            return new ResponseEntity<Page<Blog>>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<Page<Blog>>(blogs, HttpStatus.OK);
    }

    @RequestMapping(value = "/api/blogs/",produces = MediaType.APPLICATION_JSON_VALUE,method =RequestMethod.POST)
    public ResponseEntity<Blog> create(@RequestBody Blog blog) {
        blogService.save(blog);
        return new ResponseEntity<Blog>(blog, HttpStatus.OK);
    }





//    @RequestMapping(value = "/api/blogs/listBlogs/{id}", method = RequestMethod.GET)
//    public ResponseEntity<Page<Blog>> listBlogsByCategoryId(@PathVariable("id") long id, Pageable pageable) {
//        Page<Blog> blogs = blogService.findAllByCategoryId(id,pageable);
//        if (blogs.isEmpty()) {
//            return new ResponseEntity<Page<Blog>>(HttpStatus.NO_CONTENT);//You many decide to return HttpStatus.NOT_FOUND
//        }
//        return new ResponseEntity<Page<Blog>>(blogs, HttpStatus.OK);
//    }





}
