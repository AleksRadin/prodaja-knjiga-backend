package com.example.prodajaKnjigaBackend;

import com.example.prodajaKnjigaBackend.author.domain.AuthorEntity;
import com.example.prodajaKnjigaBackend.author.domain.AuthorRepository;
import com.example.prodajaKnjigaBackend.book.domain.BookEntity;
import com.example.prodajaKnjigaBackend.book.domain.BookRepository;
import com.example.prodajaKnjigaBackend.listing.BookCondition;
import com.example.prodajaKnjigaBackend.listing.domain.ListingEntity;
import com.example.prodajaKnjigaBackend.listing.domain.ListingRepository;
import com.example.prodajaKnjigaBackend.user.UserRole;
import com.example.prodajaKnjigaBackend.user.domain.UserEntity;
import com.example.prodajaKnjigaBackend.user.domain.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.*;

@SpringBootApplication
public class ProdajaKnjigaBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProdajaKnjigaBackendApplication.class, args);
    }

    @Bean
    CommandLineRunner runner(UserRepository userRepository,
                             ListingRepository listingRepository,
                             BookRepository bookRepository,
                             AuthorRepository authorRepository,
                             PasswordEncoder passwordEncoder) {
        return args -> {
            if (listingRepository.count() > 0) {
                System.out.println("Data already exists. Skipping generation.");
                return;
            }

            List<UserEntity> allUsers = new ArrayList<>();
            Object[][] userData = {
                    {"DusanGajic@gmail.com", "Dusan", "Gajic", UserRole.ADMIN, "060123456", "dusan123"},
                    {"nekonesto@gmail.com", "Aca", "Radin", UserRole.REGULAR, "0603580224", "aca123"},
                    {"admin2@gmail.com", "Laza", "Lazic", UserRole.ADMIN, "064111222", "laza123"},
                    {"stefan@gmail.com", "Stefan", "Stefanovic", UserRole.REGULAR, "065333444", "stefan123"},
                    {"marija@gmail.com", "Marija", "Markovic", UserRole.REGULAR, "063555666", "marija123"}
            };

            for (Object[] data : userData) {
                String email = (String) data[0];
                UserEntity u = userRepository.findByEmail(email).orElseGet(() -> {
                    UserEntity newUser = new UserEntity();
                    newUser.setEmail(email);
                    newUser.setFirstname((String) data[1]);
                    newUser.setLastname((String) data[2]);
                    newUser.setRole((UserRole) data[3]);
                    newUser.setPhoneNumber((String) data[4]);
                    newUser.setPassword(passwordEncoder.encode((String) data[5]));
                    return userRepository.save(newUser);
                });
                allUsers.add(u);
            }

            Map<String, AuthorEntity> authorMap = new HashMap<>();
            String[][] authors = {
                    {"Bratislav", "Petrović"}, {"Dušan", "Savić"}, {"Milan", "Martić"},
                    {"Milija", "Suknović"}, {"Marijana", "Despotović-Zrakić"}, {"Zorica", "Bogdanović"},
                    {"Dušan", "Barać"}, {"Vinka", "Filipović"}, {"Milica", "Kostić-Stajković"},
                    {"Rade", "Lazović"}, {"Siniša", "Vlajić"}, {"Maja", "Babić"}
            };

            for (String[] p : authors) {
                AuthorEntity a = new AuthorEntity();
                a.setFirstname(p[0]);
                a.setLastname(p[1]);
                authorMap.put(p[1], authorRepository.save(a));
            }

            List<BookEntity> allBooks = new ArrayList<>();

            Object[][] books = {
                    {"Baze podataka", "FON", "Petrović", "Savić"},
                    {"Projektovanje softvera", "FON", "Savić", "Vlajić"},
                    {"Operaciona istraživanja", "FON", "Martić", "Suknović"},
                    {"Elektronsko poslovanje", "FON", "Despotović-Zrakić", "Bogdanović", "Barać"},
                    {"Menadžment", "FON", "Filipović", "Kostić-Stajković"},
                    {"Matematika 1", "FON", "Lazović"},
                    {"Programski jezici", "FON", "Vlajić", "Savić", "Bogdanović"},
                    {"Engleski jezik 1", "FON", "Babić"},
                    {"Strukture podataka i algoritmi", "FON", "Vlajić", "Savić"},
                    {"Statistika", "FON", "Suknović", "Martić"}
            };

            for (Object[] book : books) {
                BookEntity b = new BookEntity();
                b.setTitle((String) book[0]);
                b.setPublisher((String) book[1]);

                Set<AuthorEntity> autoriSet = new HashSet<>();
                for (int j = 2; j < book.length; j++) {
                    String lastname = (String) book[j];
                    if (authorMap.containsKey(lastname)) {
                        autoriSet.add(authorMap.get(lastname));
                    }
                }
                b.setAuthors(autoriSet);
                allBooks.add(bookRepository.save(b));
            }

            Random random = new Random();
            System.out.println("Generating 50 listings...");

            for (int i = 0; i < 50; i++) {
                UserEntity randomOwner = allUsers.get(random.nextInt(allUsers.size()));
                ListingEntity listing = new ListingEntity();
                listing.setUser(randomOwner);
                listing.setCondition(BookCondition.values()[random.nextInt(BookCondition.values().length)]);
                listing.setCreatedAt(LocalDate.now().minusDays(random.nextInt(15)));

                double r = random.nextDouble();
                int targetBookCount = (r < 0.70) ? 3 : (r < 0.90) ? 2 : 1;

                List<BookEntity> bookPool = new ArrayList<>(allBooks);
                Collections.shuffle(bookPool);

                Set<BookEntity> selectedBooks = new HashSet<>();
                StringBuilder titleBuilder = new StringBuilder();
                double totalPrice = 0;

                for (int j = 0; j < targetBookCount; j++) {
                    BookEntity b = bookPool.get(j);
                    selectedBooks.add(b);
                    titleBuilder.append(b.getTitle());
                    if (j < targetBookCount - 1) titleBuilder.append(" + ");
                    totalPrice += (500.0 + random.nextInt(10) * 50.0);
                }

                listing.setBooks(selectedBooks);

                if (targetBookCount > 1) {
                    listing.setPrice(Math.round((totalPrice * 0.80) / 10.0) * 10.0);
                    listing.setDescription("FON BUNDLE: " + titleBuilder + ". Special discount for the set!");
                } else {
                    listing.setPrice(totalPrice);
                    listing.setDescription("Single book: " + titleBuilder);
                }

                listingRepository.save(listing);
            }

            System.out.println("Done! Database successfully populated.");
        };
    }
}