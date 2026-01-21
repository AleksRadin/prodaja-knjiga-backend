package com.example.prodajaKnjigaBackend;

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
                             PasswordEncoder passwordEncoder) {
        return args -> {
            if (listingRepository.count() > 0) {
                System.out.println("Podaci već postoje. Preskačem generisanje.");
                return;
            }

            List<UserEntity> sviKorisnici = new ArrayList<>();
            Object[][] sviKorisniciPodaci = {
                    {"DusanGajic@gmail.com", "Dusan", "Gajic", UserRole.ADMIN, "060123456", "dusan123"},
                    {"nekonesto@gmail.com", "Aca", "Radin", UserRole.REGULAR, "0603580224", "aca123"},
                    {"admin2@gmail.com", "Laza", "Lazic", UserRole.ADMIN, "064111222", "laza123"},
                    {"stefan@gmail.com", "Stefan", "Stefanovic", UserRole.REGULAR, "065333444", "stefan123"},
                    {"marija@gmail.com", "Marija", "Markovic", UserRole.REGULAR, "063555666", "marija123"}
            };

            for (Object[] podaci : sviKorisniciPodaci) {
                String email = (String) podaci[0];
                UserEntity u = userRepository.findByEmail(email).orElseGet(() -> {
                    UserEntity newUser = new UserEntity();
                    newUser.setEmail(email);
                    newUser.setFirstname((String) podaci[1]);
                    newUser.setLastname((String) podaci[2]);
                    newUser.setRole((UserRole) podaci[3]);
                    newUser.setPhoneNumber((String) podaci[4]);
                    newUser.setPassword(passwordEncoder.encode((String) podaci[5]));
                    return userRepository.save(newUser);
                });
                sviKorisnici.add(u);
            }

            String[][] fonLiterature = {
                    {"Baze podataka", "B. Petrović", "FON"},
                    {"Projektovanje softvera", "D. Savić", "FON"},
                    {"Operaciona istraživanja", "M. Martić", "FON"},
                    {"Strukture podataka i algoritmi", "M. Milić", "FON"},
                    {"Elektronsko poslovanje", "M. Despotović-Zrakić", "FON"},
                    {"Menadžment", "V. Filipović", "FON"},
                    {"Matematika 1", "R. Lazović", "FON"},
                    {"Statistika", "M. Bulajić", "FON"},
                    {"Engleski jezik 1", "M. Babić", "FON"},
                    {"Programski jezici", "S. Vlajić", "FON"}
            };

            List<BookEntity> sveKnjigeIzBaze = new ArrayList<>();
            for (String[] b : fonLiterature) {
                BookEntity book = bookRepository.findByTitleAndAuthorAndPublisher(b[0], b[1], b[2])
                        .orElseGet(() -> bookRepository.save(new BookEntity(null, b[0], b[1], b[2], null)));
                sveKnjigeIzBaze.add(book);
            }

            Random random = new Random();
            System.out.println("Generišem 50 oglasa sa ispravnom distribucijom...");

            for (int i = 0; i < 50; i++) {
                UserEntity nasumicniVlasnik = sviKorisnici.get(random.nextInt(sviKorisnici.size()));
                ListingEntity listing = new ListingEntity();
                listing.setUser(nasumicniVlasnik);
                listing.setCondition(BookCondition.values()[random.nextInt(BookCondition.values().length)]);
                listing.setCreatedAt(LocalDate.now().minusDays(random.nextInt(15)));

                double r = random.nextDouble();
                int ciljaniBrojKnjiga = (r < 0.70) ? 3 : (r < 0.90) ? 2 : 1;

                List<BookEntity> kopijaKnjiga = new ArrayList<>(sveKnjigeIzBaze);
                Collections.shuffle(kopijaKnjiga);

                Set<BookEntity> odabraneKnjige = new HashSet<>();
                StringBuilder sbNaslovi = new StringBuilder();
                double ukupnaCena = 0;

                for (int j = 0; j < ciljaniBrojKnjiga; j++) {
                    BookEntity b = kopijaKnjiga.get(j);
                    odabraneKnjige.add(b);
                    sbNaslovi.append(b.getTitle());
                    if (j < ciljaniBrojKnjiga - 1) sbNaslovi.append(" + ");
                    ukupnaCena += (500.0 + random.nextInt(10) * 50.0);
                }

                listing.setBooks(odabraneKnjige);

                if (ciljaniBrojKnjiga > 1) {
                    listing.setPrice(Math.round((ukupnaCena * 0.80) / 10.0) * 10.0);
                    listing.setDescription("FON KOMPLET: " + sbNaslovi + ". Povoljnija cena za komplet!");
                } else {
                    listing.setPrice(ukupnaCena);
                    listing.setDescription("Pojedinačna knjiga: " + sbNaslovi);
                }

                listingRepository.save(listing);
            }

            System.out.println("Gotovo! Baza je uspešno napunjena.");
        };
    }
}