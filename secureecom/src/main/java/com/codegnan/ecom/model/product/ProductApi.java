package com.codegnan.ecom.model.product;

import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@RequestMapping("/products")
public class ProductApi {

	@Autowired private ProductRepository repo;
	
	 @Value("${upload.folder}")
	    private String uploadFolder;
	
	@PostMapping
	//@RolesAllowed("ADMIN")
    @PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<Product> create(@RequestBody Product product) {
		System.out.println("!!!!!!!!!!!!!!!In product create!!!!!!!!!!!!!!!!!!");
		Product savedProduct = repo.save(product);
		URI productURI = URI.create("/products/" + savedProduct.getId());
		return ResponseEntity.created(productURI).body(savedProduct);
	}
	
	
	
	/*
	 * @PostMapping("/upload")
	 * 
	 * @PreAuthorize("hasRole('ADMIN')") public ResponseEntity<Product> createI(
	 * 
	 * @RequestPart("product") Product product,
	 * 
	 * @RequestPart("image") MultipartFile image) {
	 * 
	 * String imageUrl = null; if (image != null && !image.isEmpty()) { try { byte[]
	 * bytes = image.getBytes(); Path path = Paths.get(uploadFolder +
	 * image.getOriginalFilename());
	 * 
	 * // Create directories if they don't exist if
	 * (Files.notExists(path.getParent())) {
	 * Files.createDirectories(path.getParent()); }
	 * 
	 * Files.write(path, bytes); imageUrl = path.toString(); } catch (IOException e)
	 * { e.printStackTrace(); return ResponseEntity.badRequest().build(); } }
	 * 
	 * product.setImageUrl(imageUrl); Product savedProduct = repo.save(product); URI
	 * productURI = URI.create("/products/" + savedProduct.getId()); return
	 * ResponseEntity.created(productURI).body(savedProduct); }
	 */
	 
	@PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Product> createI(
            @RequestPart("product") String productJson,
            @RequestPart("image") MultipartFile image) throws IOException {
        
        ObjectMapper mapper = new ObjectMapper();
        Product product = mapper.readValue(productJson, Product.class);

        String imageUrl = null;
        if (image != null && !image.isEmpty()) {
            byte[] bytes = image.getBytes();
            Path path = Paths.get(uploadFolder + image.getOriginalFilename());

            // Create directories if they don't exist
            if (Files.notExists(path.getParent())) {
                Files.createDirectories(path.getParent());
            }

            Files.write(path, bytes);
            imageUrl = path.toString();
        }
        
        product.setImageUrl(imageUrl);
        Product savedProduct = repo.save(product);
        URI productURI = URI.create("/products/" + savedProduct.getId());
        return ResponseEntity.created(productURI).body(savedProduct);
    }
	 
	 
	
	@GetMapping
	@PreAuthorize("hasAnyRole('ADMIN','USER')")
	//@RolesAllowed({"ADMIN", "USER"})
	public List<Product> list() {
		return repo.findAll();
	}
}
