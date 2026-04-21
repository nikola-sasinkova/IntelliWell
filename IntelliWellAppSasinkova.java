package msoft12;

import javax.swing.*;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.*;
import java.io.*;
import java.nio.file.*;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class IntelliWellApp {

    // Enumy z mojho class diagramu

    enum Hodnotenie {
        JEDNA_HVIEZDICKA,
        DVE_HVIEZDICKY,
        TRI_HVIEZDICKY
    }

    enum Otazka {
        AKO_STE_BOLI_SPOKOJNI_S_VYSTUPOVANIM_RECEPCNEJ,
        AKO_HODNOTITE_POBYT_V_NIKAWELLI,
        AKO_HODNOTITE_NASTAVENIE_TEPLOT_HUDBY_A_OSVETLENIA
    }

    enum StavRezervacie {
        VYTVORENA,
        PRESUNUTA,
        SPOJENA,
        ANULOVANA,
        REKLAMOVANA,
        ARCHIVOVANA,
        EXPIROVANA
    }

    enum StavNikaWellu {
        PRIPRAVENY,
        PRIPRAVENY_PRE_KLIENTA,
        STABILIZOVANY,
        OPAT_STABILIZOVANY,
        PODCHLADENY,
        PREHRIATY,
        VYSUSOVANY,
        DEZINFIKOVANY,
        NEAKTIVNY
    }

    enum TypZariadenia {
        VYRIVKA,
        SAUNA,
        OSVETLENIE,
        REPRODUKTOR
    }

    enum StavVyjadreniaSaunovehoMajstra {
        NEPOTVRDENA_SAUNOVYM_MAJSTROM,
        SCHVALENA_SAUNOVYM_MAJSTROM,
        ZAMIETNUTA_SAUNOVYM_MAJSTROM
    }

    // Interfacy z mojho class diagramu

    interface VyberacProfilov {
        void vyberPozadovanyProfil();
    }

    interface KlientskyServis {
        void vyhladajKlientaVExistujucejEvidenciiKlientov();
        void zaregistrujKlienta();
        void vyberPotvrdenieKlienta();
    }

    interface RezervacnySystem {
        void vyberZobrazenieVsetkychRezervaciiVAktualnyDen();
        void vyberPozadovanuRezervaciu();
        void vyberMoznostUlozeniaVyplnenychParametrov();
        void vyberVytvorenieNovejRezervacie();
        void vyplnDatumACasRezervacie();
        void vyplnPoznamkuNaZakladePoziadaviekKlienta();
        void zvolUlozenieRezervacie();
        void vyberVyhladanieRezervacie();
        void vyhladajRezervaciuVExistujucejEvidenciiRezervacii();
        void priradSaunovehoMajstraKRezervacii();
        void vyberTypAromy();
        void vyberTypStarostlivostiOPokozku();
    }

    interface KonfiguratorNikaWellu {
        void spristupniSpusteniePripravyProstredia();
        void zacniPripravuProstredia();
        void vyplnParametreZariadeni();
        void potvrdVyplneneParametre();
        void vyberMoznostOdoslaniaParametrovZariadeniNikaWellu();
        void zaevidujInformaciuSpusteniaNastavovaniaParametrovZariadeni();
    }

    interface SaunovyDispecer {
        void zvolPriradenieSaunovehoMajstraKRezervacii();
        void vyberSaunovehoMajstra();
        void odosliPoziadavkuNaMajstra();
    }

    // Classy z mojho class diagramu

    static class Osoba {
        String meno;
        String priezvisko;
        int vek;
        String identifikator;
    }

    static class Adresa {
        String ulica;
        String mesto;
        String psc;
        String orientacneCislo;
        String supisneCislo;
        String stat;
        
        public Adresa() {
        }

        public Adresa(String ulica, String mesto, String psc,
                      String orientacneCislo, String supisneCislo, String stat) {
            this.ulica = ulica;
            this.mesto = mesto;
            this.psc = psc;
            this.orientacneCislo = orientacneCislo;
            this.supisneCislo = supisneCislo;
            this.stat = stat;
        }
      
    }

    static class Kontakt {
        String telefonneCislo;
        String email;
        
        public Kontakt() {
        }

        public Kontakt(String telefonneCislo, String email) {
            this.telefonneCislo = telefonneCislo;
            this.email = email;
        }
        
    }

    static class Kontraindikacie {
        String popisKontraindikacii;
        
        public Kontraindikacie() {
        }

        public Kontraindikacie(String popisKontraindikacii) {
            this.popisKontraindikacii = popisKontraindikacii;
        }
        
    }

    static class Klient extends Osoba {
        Adresa adresa;
        Kontakt kontakt;
        Kontraindikacie kontraindikacie;
        int technickeId;      
        
        
        private static final List<Klient> SEKVENCNA_EVIDENCIA = new ArrayList<>();
        
        public Klient() {
        }

        public Klient(String meno, String priezvisko, int vek, String identifikator) {
            this.meno = meno;
            this.priezvisko = priezvisko;
            this.vek = vek;
            this.identifikator = identifikator;
        }

        public static boolean OverExistenciuKlientaPodlaMenaAPriezviska(String meno, String priezvisko) {
            System.out.println("Overujem existenciu klienta: " + meno + " " + priezvisko);
            for (Klient k : SEKVENCNA_EVIDENCIA) {
                if (k.meno != null && k.priezvisko != null &&
                        k.meno.equalsIgnoreCase(meno) &&
                        k.priezvisko.equalsIgnoreCase(priezvisko)) {
                    System.out.println("Klient uz v evidencii je.");
                    return true;
                }
            }
            System.out.println("Klient v evidencii nie je.");
            return false;
        }

        static void pridajDoSekvencnejEvidencie(Klient klient) {
            SEKVENCNA_EVIDENCIA.add(klient);
        }

        public void priradAdresuKlientovi(Adresa adresa) {
            this.adresa = adresa;
            System.out.println("priradAdresuKlientovi -> " + adresa.ulica + ", " + adresa.mesto);
        }

        public void priradKontaktKlientovi(Kontakt kontakt) {
            this.kontakt = kontakt;
            System.out.println("priradKontaktKlientovi -> " + kontakt.telefonneCislo + ", " + kontakt.email);
        }

        public void priradKontraindikacieKlientovi(Kontraindikacie kontraindikacieKlienta) {
            this.kontraindikacie = kontraindikacieKlienta;
            System.out.println("priradKontraindikacieKlientovi -> " + kontraindikacieKlienta.popisKontraindikacii);
        }

      
        // metoda potvrdKlienta z classy Klient
        public void potvrdKlienta() {
        }

        public String celeMeno() {
            return meno + " " + priezvisko;
        }

        @Override
        public String toString() {
            return technickeId + " - " + celeMeno();
        }
    }

    static class Recepcna extends Osoba {
        Adresa adresa;
    }

    static class Aroma {
        String nazov;
        String identifikator;
    }

    static class StarostlivostOPokozku {
        String nazov;
        String identifikator;
    }

    static class Parameter {
        int pocetStupnovVSaune;
        int pocetStupnovVoVirivke;
        int pocetStupnovVSpolocnomPriestore;
    }

    static class Profil {
        Parameter parameter;
        String nazov;
    }

    // SaunovyMajster dedi z Osoba a ma vlastny Kontakt
    static class SaunovyMajster extends Osoba {
        Kontakt kontakt;
        int technickeId;

        public SaunovyMajster(int technickeId, String meno) {
            this.technickeId = technickeId;
            this.meno = meno;
            this.priezvisko = "";
            this.vek = 0;
            this.identifikator = "SM" + technickeId;
            this.kontakt = new Kontakt();
        }

        // metoda spracujPoziadavku z classy SaunovyMajster
        // UC05: Krok 12
        public void spracujPoziadavku() {
        }

        
        public static SaunovyMajster VyhladajSaunovehoMajstra(String identifikator) {
            System.out.println("Vyhladanie saunoveho majstra pre identifikator: " + identifikator);
            if (identifikator == null) {
                return null;
            }
            try {
                String cislo = identifikator.replaceAll("\\D+", "");
                int id = Integer.parseInt(cislo);
                SaunovyMajster m = new SaunovyMajster(id, "Saunovy majster " + id);
                System.out.println(" - najdeny saunovy majster: " + m);
                return m;
            } catch (NumberFormatException ex) {
                System.out.println(" - nepodarilo sa ziskat ID z identifikatora.");
                return null;
            }
        }
        
        @Override
        public String toString() {
            return technickeId + " - " + meno;
        }
    }

    static class NikaWell {
        String identifikator;
        StavNikaWellu stav;
        Zariadenie zariadenie;

        public void odosliPoziadavkuNaNastavenieParametrovZariadeni() {
        }

        public void nastavParametreNaZariadeniach() {
        }
    }

    static class Zariadenie {
        NikaWell nikawell;
        String nazov;
        TypZariadenia typZariadenia;
    }

    static class Rezervacia {
        String identifikator;
        LocalDateTime datumACasZaciatku;
        int dlzkaTrvania;
        StavRezervacie stav;
        boolean bolVyzadovanySaunovyMajster;
        StavVyjadreniaSaunovehoMajstra stavVyjadreniaSaunovehoMajstra;
        LocalDate datumExpiracie;
        LocalDate datumUlozeniaRezervacie;
        LocalDate datumZruseniaRezervacie;   
        Profil profil;
        StarostlivostOPokozku starostlivostOPokozku;
        Recepcna recepcna;
        Klient klient;
        SaunovyMajster saunovyMajster;
        NikaWell nikaWell;

        int technickeId;
        String poznamka;
        Parameter parameter;
        Aroma aroma;

        // vytvorenie rezervacie
        public Rezervacia(int technickeId,
                          String identifikator,
                          Klient klient,
                          LocalDate datum,
                          LocalTime cas,
                          int dlzkaTrvania,
                          String poznamka) {
            this.technickeId = technickeId;
            this.identifikator = identifikator;
            this.klient = klient;
            this.datumACasZaciatku = LocalDateTime.of(datum, cas);
            this.dlzkaTrvania = dlzkaTrvania;
            this.poznamka = poznamka;
            this.stav = StavRezervacie.VYTVORENA;
            this.datumUlozeniaRezervacie = LocalDate.now();
        }

        public LocalDate getDatum() {
            return datumACasZaciatku.toLocalDate();
        }

        public LocalTime getCas() {
            return datumACasZaciatku.toLocalTime();
        }

        public LocalDateTime getKoniec() {
            return datumACasZaciatku.plusMinutes(dlzkaTrvania);
        }

        public void vytvorRezervaciu() {
        }

        public void ulozRezervaciu() {
        }

        public void zobrazPozadovanuRezervaciu() {
        }

        // v mojom class diagrame, class Rezervacia ma metodu Prirad klienta k rezervacii()
        // UC01: Krok 6
        public void priradKlientaKRezervacii(Klient klient) {
            this.klient = klient;
        }

        // v mojom class diagrame, class Rezervacia ma metodu Uloz parametre zariadeni v rezervacii()
        // UC03: Krok 9
        public void ulozParametreZariadeniVRezervacii(Parameter parameter) {
            this.parameter = parameter;
        }

        public void ulozParametreZariadeniSRezervaciou(Parameter parameter) {
            ulozParametreZariadeniVRezervacii(parameter);
        }

        public void nacitajPozadovanuRezervaciu() {
        }

        // v mojom class diagrame, class Rezervacia ma metodu Prirad k rezervacii vyplneny termin a cas()
        // UC01: Krok 8
        public void priradKRezervaciiVyplnenyTerminACas(LocalDate datum, LocalTime cas) {
            this.datumACasZaciatku = LocalDateTime.of(datum, cas);
        }

        public void nacitajRezervaciu() {
        }

        // v mojom class diagrame, class Rezervacia ma metodu Uloz priradenie do rezervacie()
        // UC05: Krok 13
        public void ulozPriradenieDoRezervacie(SaunovyMajster majster) {
            this.saunovyMajster = majster;
        }

        // v mojom class diagrame, class Rezervacia ma metodu Zapamataj si vybrany typ aromy()
        // UC05: Krok 9
        public void zapamatajSiVybranyTypAromy(Aroma a) {
            this.aroma = a;
        }

        // v mojom class diagrame, class Rezervacia ma metodu Zapamataj si vybrany typ starostlivosti o pokozku()
        // UC05: Krok 11
        public void zapamatajSiVybranyTypStarostlivostiOPokozku(StarostlivostOPokozku s) {
            this.starostlivostOPokozku = s;
        }
        
        public void vyberRezervaciu() {
        }

        public boolean OverExistenciuRezervaciiPreSaunovehoMajstraVRozsahuDatumov(
                SaunovyMajster saunovyMajster,
                LocalDateTime datumZaciatku,
                LocalDateTime datumKonca) {

            System.out.println("Overujem rezervaciu pre saunoveho majstra " +
                    (saunovyMajster != null ? saunovyMajster.identifikator : "null") +
                    " v rozsahu " + datumZaciatku + " - " + datumKonca);

            if (saunovyMajster == null || this.saunovyMajster == null) {
                return false;
            }

            if (this.saunovyMajster.technickeId != saunovyMajster.technickeId) {
                return false;
            }

            LocalDateTime existZaciatok = this.datumACasZaciatku;
            LocalDateTime existKoniec = this.getKoniec();

            boolean overlap = !(datumKonca.isBefore(existZaciatok) ||
                    datumZaciatku.isAfter(existKoniec));

            System.out.println(" - prekryv = " + overlap);
            return overlap;
        }
        
        
        @Override
        public String toString() {
            return "ID=" + technickeId + " | " + getDatum() + " " + getCas();
        }
    }

    static class SpojenaRezervacia {
        Rezervacia rezervacia;
        Rezervacia spojenaRezervacia;
    }

    // spravcovia z mojho class diagramu

    // class SpravcaProfilov realizuje interface VyberacProfilov 
    static class SpravcaProfilov implements VyberacProfilov {

        @Override
        public void vyberPozadovanyProfil() {
        }
    }
    
    // class SpravcaKlientov realizuje interface KlientskyServis a sprostredkovava operacie s klientami	
    static class SpravcaKlientov implements KlientskyServis {

        HlavneOkno gui;

        public SpravcaKlientov(HlavneOkno gui) {
            this.gui = gui;
        }

        @Override
        public void vyhladajKlientaVExistujucejEvidenciiKlientov() {
        }

        @Override
        public void zaregistrujKlienta() {
        }

        @Override
        public void vyberPotvrdenieKlienta() {
        }
        

        public Klient zaregistrujKlienta(String meno,
                                         String priezvisko,
                                         int vek,
                                         String identifikator,
                                         String ulica,
                                         String mesto,
                                         String psc,
                                         String orientacneCislo,
                                         String supisneCislo,
                                         String stat) {

            System.out.println("Registracia pre: " + meno + " " + priezvisko);

            boolean existuje =
                    Klient.OverExistenciuKlientaPodlaMenaAPriezviska(meno, priezvisko);

            if (existuje) {
                OznamChybuKlientUzExistujeVSysteme();
                return null;
            }

            Klient klient = new Klient(meno, priezvisko, vek, identifikator);

            Adresa adresa = new Adresa(ulica, mesto, psc, orientacneCislo, supisneCislo, stat);
            klient.priradAdresuKlientovi(adresa);

            Kontakt kontakt = new Kontakt("", "");
            klient.priradKontaktKlientovi(kontakt);

            String popisKontraindikacii = null;
            if (popisKontraindikacii != null) {
                Kontraindikacie kontra = new Kontraindikacie(popisKontraindikacii);
                klient.priradKontraindikacieKlientovi(kontra);
            }

            ulozKlienta(klient);

            return klient;
        }

        private void OznamChybuKlientUzExistujeVSysteme() {
            System.out.println("Chyba: Klient uz existuje v systeme.");
        }

        private void ulozKlienta(Klient klient) {
            System.out.println("Ulozenie " + (klient != null ? klient.celeMeno() : "null"));
            if (klient != null) {
                Klient.pridajDoSekvencnejEvidencie(klient);
            }
        }
        
    }

    // class SpravcaRezervacii realizuje interface RezervacnySystem a pracuje s classou Rezervacia
    static class SpravcaRezervacii implements RezervacnySystem {

        HlavneOkno gui;

        public SpravcaRezervacii(HlavneOkno gui) {
            this.gui = gui;
        }

        
        // v mojom class diagrame, class SpravcaRezervacii ma metodu Vyber zobrazenie vsetkych rezervacii v aktualny den
        // UC03: Krok 1
        @Override
        public void vyberZobrazenieVsetkychRezervaciiVAktualnyDen() {
            gui.obnovZoznamRezervaciiPreUc03();
        }

        @Override
        public void vyberPozadovanuRezervaciu() {
        }

        @Override
        public void vyberMoznostUlozeniaVyplnenychParametrov() {
        }

        public void poskytniOdoslanieParametrovDoZariadeni() {
        }

        @Override
        public void vyberVytvorenieNovejRezervacie() {
            gui.prepnina(HlavneOkno.KARTA_UC01_NAJDI_KLIENTA);
        }

        public void zobrazVyberKlienta() {
            gui.prepnina(HlavneOkno.KARTA_UC01_NAJDI_KLIENTA);
        }

        @Override
        public void vyplnDatumACasRezervacie() {
            gui.prepnina(HlavneOkno.KARTA_UC01_VYBER_TERMIN);
        }

        // v mojom class diagrame, class SpravcaRezervacii ma metodu Poskytni zadanie poznamky()
        // UC01: Krok 8
        public void poskytniZadaniePoznamky() {
            gui.prepnina(HlavneOkno.KARTA_UC01_POZNAMKA);
        }

        @Override
        public void vyplnPoznamkuNaZakladePoziadaviekKlienta() {
        }

        public void zaevidujDoplnujuceInformacie() {
        }

        // v mojom class diagrame, class SpravcaRezervacii ma metodu Zobraz sumarizaciu rezervacie()
        // UC01: Krok 10
        public void zobrazSumarizaciuRezervacie() {
            gui.prepnina(HlavneOkno.KARTA_UC01_SUMAR);
        }

        public void vyziadajUlozenieRezervacie() {
        }

        @Override
        public void zvolUlozenieRezervacie() {
        }

        public void overDostupnostTerminu() {
        }

        @Override
        public void vyberVyhladanieRezervacie() {
            gui.prepnina(HlavneOkno.KARTA_UC05_VYHLADANIE_REZ);
        }

        // v mojom class diagrame, class SpravcaRezervacii ma metodu Zobraz vyhladanie rezervacie()
        // UC05: Krok 1a2
        public void zobrazVyhladanieRezervacie() {
            gui.prepnina(HlavneOkno.KARTA_UC05_VYHLADANIE_REZ);
        }

        @Override
        public void vyhladajRezervaciuVExistujucejEvidenciiRezervacii() {
        }

        public void ponukniVyberTypuAromy() {
        }

        // v mojom class diagrame, class SpravcaRezervacii ma metodu Prirad saunoveho majstra k rezervacii()
        // UC05: Krok 6
        @Override
        public void priradSaunovehoMajstraKRezervacii() {
            gui.prepnina(HlavneOkno.KARTA_UC05_VYBER_MAJSTRA);
        }

        @Override
        public void vyberTypAromy() {
        }

        public void ponukniVyberTypuStarostlivostiOPokozku() {
        }

        @Override
        public void vyberTypStarostlivostiOPokozku() {
        }

        public void zaevidujPriradenieSaunovehoMajstraKRezervacii() {
        }

        public void zobrazInformaciuOZamietnutiRezervacie() {
        }

        // v mojom class diagrame, class SpravcaRezervacii ma metodu Zobraz informaciu o nedostupnosti saunoveho majstra()
        // UC05: Krok 5a1
        public void zobrazInformaciuONedostupnostiSaunovehoMajstra() {
            gui.prepnina(HlavneOkno.KARTA_UC05_MAJSTER_NEDOSTUPNY);
        }
        
        
        public boolean OverDostupnostTerminu(SaunovyMajster saunovyMajster,
                LocalDateTime datumZaciatku,
                LocalDateTime datumKonca) {

        	System.out.println("Over dostupnost terminu pre saunoveho majstra " +
        			(saunovyMajster != null ? saunovyMajster.identifikator : "null") +
        			" v rozsahu " + datumZaciatku + " - " + datumKonca);

        	Rezervacia existujuca = new Rezervacia(
        			999,
        			"R999",
        			null,
        			datumZaciatku.toLocalDate(),
        			datumZaciatku.toLocalTime(),
        			60,
        			"Rezervacia"
        			);
        	existujuca.saunovyMajster = saunovyMajster;

        	boolean jeObsadeny =
        			existujuca.OverExistenciuRezervaciiPreSaunovehoMajstraVRozsahuDatumov(
        					saunovyMajster, datumZaciatku, datumKonca);
        	
        	return jeObsadeny;
        }
      
    }

    // class SpravcaSaunovehoMajstra realizuje interface SaunovyDispecer 
    static class SpravcaSaunovehoMajstra implements SaunovyDispecer {

        HlavneOkno gui;

        public SpravcaSaunovehoMajstra(HlavneOkno gui) {
            this.gui = gui;
        }

        // v mojom class diagrame, class SpravcaSaunovehoMajstra ma metodu Zvol priradenie saunoveho majstra k rezervacii()
        // UC05: Krok 1
        @Override
        public void zvolPriradenieSaunovehoMajstraKRezervacii() {
            gui.prepnina(HlavneOkno.KARTA_UC05_VYBER_MAJSTRA);
        }

        public void zaevidujPoziadavkuNaVyberSaunovehoMajstra() {
        }

        // v mojom class diagrame, class SpravcaSaunovehoMajstra ma metodu Ponukni vyber saunoveho majstra k rezervacii()
        // UC05: Krok 3
        public void ponukniVyberSaunovehoMajstraKRezervacii() {
            gui.prepnina(HlavneOkno.KARTA_UC05_VYBER_MAJSTRA);
        }

        @Override
        public void vyberSaunovehoMajstra() {
        }

        @Override
        public void odosliPoziadavkuNaMajstra() {
        }
        
        public SaunovyMajster VyberSaunovehoMajstra(LocalDateTime datumZaciatku,
                LocalDateTime datumKonca,
                String identifikator) {

        	if (datumZaciatku == null || datumKonca == null ||
        			!datumZaciatku.isBefore(datumKonca)) {
        		OznamChybuDatumZaciatkuJePoDatumeKonca();
        		return null;
        	}

        	SaunovyMajster saunovyMajster = SaunovyMajster.VyhladajSaunovehoMajstra(identifikator);

        	if (saunovyMajster == null) {
        		OznamChybuSaunovyMajsterNeexistuje();
        		return null;
        	}

        	SpravcaRezervacii spravcaRezervacii = new SpravcaRezervacii(null);
        	boolean jeSaunovyMajsterObsadeny =
        			spravcaRezervacii.OverDostupnostTerminu(saunovyMajster, datumZaciatku, datumKonca);

        	if (jeSaunovyMajsterObsadeny) {
        		OznamChybuSaunovyMajsterJeObsadeny();
        		return null;
        	}

        	return saunovyMajster;
        }

        private void OznamChybuDatumZaciatkuJePoDatumeKonca() {
        	System.out.println("Chyba: Datum zaciatku je po datume konca.");
        }

        private void OznamChybuSaunovyMajsterNeexistuje() {
        	System.out.println("Chyba: Saunovy majster s danym identifikatorom neexistuje.");
        }

        private void OznamChybuSaunovyMajsterJeObsadeny() {
        	System.out.println("Chyba: Saunovy majster je v danom termine obsadeny.");
        }

        
    }

    // class SpravcaNikaWellu realizuje interface KonfiguratorNikaWellu 
    static class SpravcaNikaWellu implements KonfiguratorNikaWellu {

        @Override
        public void spristupniSpusteniePripravyProstredia() {
        }

        @Override
        public void zacniPripravuProstredia() {
        }

        public void vykonajBezpecnostnuKontroluMiestnosti() {
        }

        public void poskytniInformaciuOVysledkuKontroly() {
        }

        public void zobrazNastavenieParametrovZariadeni() {
        }

        @Override
        public void vyplnParametreZariadeni() {
        }

        @Override
        public void potvrdVyplneneParametre() {
        }

        @Override
        public void vyberMoznostOdoslaniaParametrovZariadeniNikaWellu() {
        }

        public void poskytniZaevidovanieInformacieOPrebiehajucomNastavovaniZariadeni() {
        }

        @Override
        public void zaevidujInformaciuSpusteniaNastavovaniaParametrovZariadeni() {
        }

        public void ulozInformaciuOSpusteniNastavovaniaParametrovNaZariadeniach() {
        }
    }

    static class DatabazaSubory {

        private static final String SUBOR_KLIENTI = "klienti.txt";
        private static final String SUBOR_REZERVACIE = "rezervacie.txt";
        private static final String SUBOR_LOG = "nika_log.txt";

        private static final DateTimeFormatter DTF_DATUM = DateTimeFormatter.ISO_LOCAL_DATE;
        private static final DateTimeFormatter DTF_CAS = DateTimeFormatter.ofPattern("HH:mm");

        private static String safe(String s) {
            return s == null ? "" : s;
        }

        public static List<Klient> nacitajKlientov() {
            List<Klient> list = new ArrayList<>();
            if (!Files.exists(Paths.get(SUBOR_KLIENTI))) return list;

            try (BufferedReader br = Files.newBufferedReader(Paths.get(SUBOR_KLIENTI))) {
                String line;
                while ((line = br.readLine()) != null) {
                    String[] p = line.split("\\|", -1);
                    if (p.length >= 13) {
                        int i = 0;
                        int id = Integer.parseInt(p[i++]);
                        String meno = p[i++];
                        String priezvisko = p[i++];
                        int vek = Integer.parseInt(p[i++]);

                        Adresa adresa = new Adresa();
                        adresa.ulica = p[i++];
                        adresa.mesto = p[i++];
                        adresa.psc = p[i++];
                        adresa.orientacneCislo = p[i++];
                        adresa.supisneCislo = p[i++];
                        adresa.stat = p[i++];

                        Kontakt kontakt = new Kontakt();
                        kontakt.telefonneCislo = p[i++];
                        kontakt.email = p[i++];

                        Kontraindikacie kontra = new Kontraindikacie();
                        kontra.popisKontraindikacii = p[i++];

                        Klient k = new Klient();
                        k.technickeId = id;
                        k.meno = meno;
                        k.priezvisko = priezvisko;
                        k.vek = vek;
                        k.identifikator = String.valueOf(id);
                        k.adresa = adresa;
                        k.kontakt = kontakt;
                        k.kontraindikacie = kontra;

                        list.add(k);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return list;
        }

        public static void ulozKlientov(List<Klient> klienti) {
            try (BufferedWriter bw = Files.newBufferedWriter(Paths.get(SUBOR_KLIENTI))) {
                for (Klient k : klienti) {
                    bw.write(
                            k.technickeId + "|" +
                                    safe(k.meno) + "|" +
                                    safe(k.priezvisko) + "|" +
                                    k.vek + "|" +
                                    safe(k.adresa != null ? k.adresa.ulica : "") + "|" +
                                    safe(k.adresa != null ? k.adresa.mesto : "") + "|" +
                                    safe(k.adresa != null ? k.adresa.psc : "") + "|" +
                                    safe(k.adresa != null ? k.adresa.orientacneCislo : "") + "|" +
                                    safe(k.adresa != null ? k.adresa.supisneCislo : "") + "|" +
                                    safe(k.adresa != null ? k.adresa.stat : "") + "|" +
                                    safe(k.kontakt != null ? k.kontakt.telefonneCislo : "") + "|" +
                                    safe(k.kontakt != null ? k.kontakt.email : "") + "|" +
                                    safe(k.kontraindikacie != null ? k.kontraindikacie.popisKontraindikacii : "")
                    );
                    bw.newLine();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public static List<Rezervacia> nacitajRezervacie(List<Klient> klienti) {
            List<Rezervacia> list = new ArrayList<>();
            if (!Files.exists(Paths.get(SUBOR_REZERVACIE))) return list;

            try (BufferedReader br = Files.newBufferedReader(Paths.get(SUBOR_REZERVACIE))) {
                String line;
                while ((line = br.readLine()) != null) {
                    String[] p = line.split("\\|", -1);
                    if (p.length >= 14) {
                        int i = 0;
                        int id = Integer.parseInt(p[i++]);
                        int klientId = Integer.parseInt(p[i++]);
                        LocalDate datum = LocalDate.parse(p[i++], DTF_DATUM);
                        LocalTime cas = LocalTime.parse(p[i++], DTF_CAS);
                        int dlzka = Integer.parseInt(p[i++]);
                        String poznamka = p[i++];
                        StavRezervacie stav = StavRezervacie.valueOf(p[i++]);
                        boolean bolMajster = Boolean.parseBoolean(p[i++]);
                        String sStavVyj = p[i++];
                        StavVyjadreniaSaunovehoMajstra stavVyj =
                                sStavVyj.isEmpty() ? null : StavVyjadreniaSaunovehoMajstra.valueOf(sStavVyj);
                        String stVir = p[i++];
                        String stSau = p[i++];
                        String stSpol = p[i++];
                        String aromaNazov = p[i++];
                        String starNazov = (i < p.length ? p[i++] : "");
                        String sDatumZrusenia = (i < p.length ? p[i++] : "");

                        Klient klient = null;
                        for (Klient kk : klienti) {
                            if (kk.technickeId == klientId) {
                                klient = kk;
                                break;
                            }
                        }

                        Rezervacia r = new Rezervacia(
                                id,
                                "R" + id,
                                klient,
                                datum,
                                cas,
                                dlzka,
                                poznamka
                        );
                        r.stav = stav;
                        r.bolVyzadovanySaunovyMajster = bolMajster;
                        r.stavVyjadreniaSaunovehoMajstra = stavVyj;

                        Parameter param = new Parameter();
                        param.pocetStupnovVoVirivke = stVir.isEmpty() ? 0 : Integer.parseInt(stVir);
                        param.pocetStupnovVSaune = stSau.isEmpty() ? 0 : Integer.parseInt(stSau);
                        param.pocetStupnovVSpolocnomPriestore = stSpol.isEmpty() ? 0 : Integer.parseInt(stSpol);
                        r.parameter = param;

                        if (!aromaNazov.isEmpty()) {
                            Aroma a = new Aroma();
                            a.nazov = aromaNazov;
                            r.aroma = a;
                        }
                        if (!starNazov.isEmpty()) {
                            StarostlivostOPokozku s = new StarostlivostOPokozku();
                            s.nazov = starNazov;
                            r.starostlivostOPokozku = s;
                        }
                        if (!sDatumZrusenia.isEmpty()) {
                            r.datumZruseniaRezervacie = LocalDate.parse(sDatumZrusenia, DTF_DATUM);
                        }

                        list.add(r);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return list;
        }

        public static void ulozRezervacie(List<Rezervacia> rezervacie) {
            try (BufferedWriter bw = Files.newBufferedWriter(Paths.get(SUBOR_REZERVACIE))) {
                for (Rezervacia r : rezervacie) {
                    bw.write(
                            r.technickeId + "|" +
                                    (r.klient != null ? r.klient.technickeId : 0) + "|" +
                                    r.getDatum().format(DTF_DATUM) + "|" +
                                    r.getCas().format(DTF_CAS) + "|" +
                                    r.dlzkaTrvania + "|" +
                                    safe(r.poznamka) + "|" +
                                    (r.stav != null ? r.stav.name() : StavRezervacie.VYTVORENA.name()) + "|" +
                                    r.bolVyzadovanySaunovyMajster + "|" +
                                    (r.stavVyjadreniaSaunovehoMajstra != null ? r.stavVyjadreniaSaunovehoMajstra.name() : "") + "|" +
                                    (r.parameter != null ? r.parameter.pocetStupnovVoVirivke : 0) + "|" +
                                    (r.parameter != null ? r.parameter.pocetStupnovVSaune : 0) + "|" +
                                    (r.parameter != null ? r.parameter.pocetStupnovVSpolocnomPriestore : 0) + "|" +
                                    (r.aroma != null ? safe(r.aroma.nazov) : "") + "|" +
                                    (r.starostlivostOPokozku != null ? safe(r.starostlivostOPokozku.nazov) : "") + "|" +
                                    (r.datumZruseniaRezervacie != null ? r.datumZruseniaRezervacie.format(DTF_DATUM) : "")
                    );
                    bw.newLine();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public static void zapisLog(String sprava) {
            try (BufferedWriter bw = Files.newBufferedWriter(Paths.get(SUBOR_LOG),
                    StandardOpenOption.CREATE, StandardOpenOption.APPEND)) {
                bw.write(LocalDateTime.now() + " - " + sprava);
                bw.newLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    static class HlavneOkno extends JFrame {

        CardLayout cardLayout = new CardLayout();
        JPanel cards = new JPanel(cardLayout);

        private void stylePrimaryButton(JButton button) {
            button.setBackground(new Color(0, 102, 204));
            button.setForeground(Color.WHITE);
            button.setFocusPainted(false);
            button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        }

        private void styleSecondaryButton(JButton button) {
            button.setBackground(new Color(220, 220, 220));
            button.setForeground(Color.BLACK);
            button.setFocusPainted(false);
            button.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        }

        static final String KARTA_DOMOV = "domov";

        // UC01
        static final String KARTA_UC01_NAJDI_KLIENTA = "uc01_najdi_klienta";
        static final String KARTA_UC01_KLIENT_NAJDENY = "uc01_klient_najdeny";
        static final String KARTA_UC01_KLIENT_NENAJDENY = "uc01_klient_nenajdeny";
        static final String KARTA_UC01_VYBER_TERMIN = "uc01_vyber_termin";
        static final String KARTA_UC01_POZNAMKA = "uc01_poznamka";
        static final String KARTA_UC01_SUMAR = "uc01_sumar";
        static final String KARTA_UC01_VYTVORENA = "uc01_vytvorena";

        // UC02
        static final String KARTA_UC02_REG_OSOBA_ADRESA = "uc02_reg_osoba_adresa";
        static final String KARTA_UC02_REG_KONTRA = "uc02_reg_kontra";
        static final String KARTA_UC02_REG_KONTAKT = "uc02_reg_kontakt";
        static final String KARTA_UC02_REG_SUMAR = "uc02_reg_sumar";
        static final String KARTA_UC02_KLIENT_EXISTUJE = "uc02_klient_existuje";

        // UC05
        static final String KARTA_UC05_VYBER_MAJSTRA = "uc05_vyber_majstra";
        static final String KARTA_UC05_MAJSTER_NEDOSTUPNY = "uc05_majster_nedostupny";
        static final String KARTA_UC05_MAJSTER_DOSTUPNY = "uc05_majster_dostupny";
        static final String KARTA_UC05_VYBER_AROMY = "uc05_vyber_aromy";
        static final String KARTA_UC05_VYBER_STAROSTLIVOSTI = "uc05_vyber_starostlivosti";
        static final String KARTA_UC05_CAKANIE_SCHVALENIE = "uc05_cakanie_schvalenie";
        
        static final String KARTA_UC05_VYHLADANIE_REZ = "uc05_vyhladanie_rez";
        static final String KARTA_UC05_DETAIL_REZ = "uc05_detail_rez";

        // UC03 
        static final String KARTA_UC03_ZOZNAM_REZ = "uc03_zoznam_rez";
        static final String KARTA_UC03_DETAIL_REZ = "uc03_detail_rez";
        static final String KARTA_UC03_BEZPECNOST = "uc03_bezpecnost";
        static final String KARTA_UC03_PARAMETRE = "uc03_parametre";
        static final String KARTA_UC03_ODOSLAT = "uc03_odoslat";
        static final String KARTA_UC03_PARAM_NASTAVENE = "uc03_param_nastavene";
        static final String KARTA_UC03_EVIDENCIA = "uc03_evidencia";
        static final String KARTA_UC03_EVIDENCIA_MUSI = "uc03_evidencia_musi";
        static final String KARTA_UC03_EVIDENCIA_HOTOVO = "uc03_evidencia_hotovo";

        List<Klient> klienti;
        List<Rezervacia> rezervacie;
        SaunovyMajster jedinyMajster;

        SpravcaRezervacii spravcaRezervacii;
        SpravcaKlientov spravcaKlientov;
        SpravcaSaunovehoMajstra spravcaSaunovehoMajstra;
        SpravcaNikaWellu spravcaNikaWellu;
        SpravcaProfilov spravcaProfilov;

        Klient vybranyKlient;
        Rezervacia vybranaRezervacia;
        boolean poRegistraciiPokracovatVRezervacii = false;
        int pocetPoziadaviekNaSaunovehoMajstra = 0;

        // UC01
        JTextField txtUc01MenoPriezvisko;
        JLabel lblUc01KlientOtazka;
        JSpinner spUc01Datum;
        JComboBox<String> cbUc01Cas;
        JTextArea taUc01Poznamka;
        JTextArea taUc01Sumar;
        JLabel lblUc01VytvorenaText;

        // UC02 
        JTextField txtRegMeno;
        JTextField txtRegPriezvisko;
        JSpinner spRegVek;
        JTextField txtRegUlica;
        JTextField txtRegMesto;
        JTextField txtRegPsc;
        JTextField txtRegOrientacne;
        JTextField txtRegSupisne;
        JTextField txtRegStat;
        JTextArea taRegKontra;
        JTextField txtRegTelefon;
        JTextField txtRegEmail;
        JTextArea taUc02RegSumar;

        String regMeno, regPriezvisko;
        int regVek;
        String regUlica, regMesto, regPsc, regOrientacne, regSupisne, regStat;
        String regTelefon, regEmail, regKontra;

        // UC02 vyhladanie rezervacie
        JTextField txtUc05VyhlId;
        
        JEditorPane taUc02DetailRez;
        JEditorPane taUc03DetailRez;

        // UC05
        JComboBox<SaunovyMajster> cbUc05Majster;
        JLabel lblUc05DostupnostOtazka;
        JComboBox<String> cbUc05Aroma;
        JComboBox<String> cbUc05Star;
        JLabel lblUc05CakanieText;
        JButton btnUc05CakanieOk;

        // UC03
        DefaultListModel<Rezervacia> modelUc03Rezervacie;
        JList<Rezervacia> listUc03Rezervacie;
        JLabel lblUc03Bezpecnost;
        JComboBox<String> cbUc03Profil;
        JSpinner spUc03Vyrivka;
        JSpinner spUc03Sauna;
        JSpinner spUc03Spolocny;

        public HlavneOkno() {
            super("IntelliWell");

            // inicializacia dat pri spusteni aplikacie
            klienti = DatabazaSubory.nacitajKlientov();
            rezervacie = DatabazaSubory.nacitajRezervacie(klienti);

            jedinyMajster = new SaunovyMajster(1, "Ján Malík");
            for (Rezervacia r : rezervacie) {
                if (r != null
                        && r.bolVyzadovanySaunovyMajster
                        && r.stavVyjadreniaSaunovehoMajstra ==
                           StavVyjadreniaSaunovehoMajstra.SCHVALENA_SAUNOVYM_MAJSTROM) {
                    r.saunovyMajster = jedinyMajster;
                }
            }

            spravcaRezervacii = new SpravcaRezervacii(this);
            spravcaKlientov = new SpravcaKlientov(this);
            spravcaSaunovehoMajstra = new SpravcaSaunovehoMajstra(this);
            spravcaNikaWellu = new SpravcaNikaWellu();
            spravcaProfilov = new SpravcaProfilov();

            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            setSize(950, 650);
            setLocationRelativeTo(null);

            getContentPane().setLayout(new BorderLayout());
            getContentPane().add(cards, BorderLayout.CENTER);

            // domov
            cards.add(vytvorDomovPanel(), KARTA_DOMOV);

            // UC01 obrazovky
            cards.add(vytvorUc01NajdiKlientaPanel(), KARTA_UC01_NAJDI_KLIENTA);
            cards.add(vytvorUc01KlientNajdenyPanel(), KARTA_UC01_KLIENT_NAJDENY);
            cards.add(vytvorUc01KlientNenajdenyPanel(), KARTA_UC01_KLIENT_NENAJDENY);
            cards.add(vytvorUc01VyberTerminPanel(), KARTA_UC01_VYBER_TERMIN);
            cards.add(vytvorUc01PoznamkaPanel(), KARTA_UC01_POZNAMKA);
            cards.add(vytvorUc01SumarPanel(), KARTA_UC01_SUMAR);
            cards.add(vytvorUc01VytvorenaPanel(), KARTA_UC01_VYTVORENA);

            // UC02 obrazovky
            cards.add(vytvorUc02RegOsobaAdresaPanel(), KARTA_UC02_REG_OSOBA_ADRESA);
            cards.add(vytvorUc02RegKontraPanel(), KARTA_UC02_REG_KONTRA);
            cards.add(vytvorUc02RegKontaktPanel(), KARTA_UC02_REG_KONTAKT);
            cards.add(vytvorUc02RegSumarPanel(), KARTA_UC02_REG_SUMAR);
            cards.add(vytvorUc02KlientExistujePanel(), KARTA_UC02_KLIENT_EXISTUJE);

            // UC05 obrazovky
            cards.add(vytvorUc05VyberMajstraPanel(), KARTA_UC05_VYBER_MAJSTRA);
            cards.add(vytvorUc05MajsterNedostupnyPanel(), KARTA_UC05_MAJSTER_NEDOSTUPNY);
            cards.add(vytvorUc05MajsterDostupnyPanel(), KARTA_UC05_MAJSTER_DOSTUPNY);
            cards.add(vytvorUc05VyberAromyPanel(), KARTA_UC05_VYBER_AROMY);
            cards.add(vytvorUc05VyberStarostlivostiPanel(), KARTA_UC05_VYBER_STAROSTLIVOSTI);
            cards.add(vytvorUc05CakanieSchvaleniePanel(), KARTA_UC05_CAKANIE_SCHVALENIE);
            
            cards.add(vytvorUc02VyhladanieRezPanel(), KARTA_UC05_VYHLADANIE_REZ);
            cards.add(vytvorUc02DetailRezPanel(), KARTA_UC05_DETAIL_REZ);

            // UC03 obrazovky
            cards.add(vytvorUc03ZoznamRezPanel(), KARTA_UC03_ZOZNAM_REZ);
            cards.add(vytvorUc03DetailRezPanel(), KARTA_UC03_DETAIL_REZ);
            cards.add(vytvorUc03BezpecnostPanel(), KARTA_UC03_BEZPECNOST);
            cards.add(vytvorUc03ParametrePanel(), KARTA_UC03_PARAMETRE);
            cards.add(vytvorUc03OdoslatPanel(), KARTA_UC03_ODOSLAT);
            cards.add(vytvorUc03ParamNastavenePanel(), KARTA_UC03_PARAM_NASTAVENE);
            cards.add(vytvorUc03EvidenciaPanel(), KARTA_UC03_EVIDENCIA);
            cards.add(vytvorUc03EvidenciaMusiPanel(), KARTA_UC03_EVIDENCIA_MUSI);
            cards.add(vytvorUc03EvidenciaHotovoPanel(), KARTA_UC03_EVIDENCIA_HOTOVO);

            prepnina(KARTA_DOMOV);
        }

        void prepnina(String karta) {
            cardLayout.show(cards, karta);
        }

        // domov
        // UC01: Krok 1
        // UC02: Krok 1
        // UC03: Krok 1
        // UC05: Krok 1a1
        private JPanel vytvorDomovPanel() {
            JPanel p = new JPanel(new BorderLayout());

            JLabel hl = new JLabel("IntelliWell", SwingConstants.CENTER);
            hl.setFont(new Font("Segoe UI", Font.BOLD, 28));
            p.add(hl, BorderLayout.NORTH);

            JLabel popis = new JLabel(
                    "Zvoľte operáciu, ktorú chcete ako recepčná vykonať.",
                    SwingConstants.CENTER);
            popis.setFont(new Font("Segoe UI", Font.PLAIN, 16));
            p.add(popis, BorderLayout.SOUTH);

            JPanel center = new JPanel(new GridLayout(4, 1, 10, 10));
            center.setBorder(BorderFactory.createEmptyBorder(20, 200, 20, 200));

            JButton btnUc01 = new JButton("Vytvorenie rezervácie");
            stylePrimaryButton(btnUc01);

            JButton btnVyhlRez = new JButton("Vyhľadanie rezervácie");
            stylePrimaryButton(btnVyhlRez);

            JButton btnRegKlient = new JButton("Zaregistrovanie klienta");
            stylePrimaryButton(btnRegKlient);

            JButton btnUc03 = new JButton("Pripravenie prostredia NikaWell-u");
            stylePrimaryButton(btnUc03);

            btnUc01.setToolTipText("Vytvorenie novej rezervacie pre klienta");
            btnVyhlRez.setToolTipText("Vyhladanie existujucej rezervacie");
            btnRegKlient.setToolTipText("Registracia noveho klienta");
            btnUc03.setToolTipText("Priprava prostredia NikaWell-u pre rezervaciu");

            btnUc01.addActionListener(e -> {
                vybranyKlient = null;
                txtUc01MenoPriezvisko.setText("");
                prepnina(KARTA_UC01_NAJDI_KLIENTA);
            });

            btnVyhlRez.addActionListener(e -> {
                txtUc05VyhlId.setText("");
                taUc02DetailRez.setText("");
                prepnina(KARTA_UC05_VYHLADANIE_REZ);
            });

            btnRegKlient.addActionListener(e -> {
                poRegistraciiPokracovatVRezervacii = false;
                vynulujRegistraciu();
                prepnina(KARTA_UC02_REG_OSOBA_ADRESA);
            });

            btnUc03.addActionListener(e -> {
                obnovZoznamRezervaciiPreUc03();
                prepnina(KARTA_UC03_ZOZNAM_REZ);
            });

            center.add(btnUc01);
            center.add(btnVyhlRez);
            center.add(btnRegKlient);
            center.add(btnUc03);

            p.add(center, BorderLayout.CENTER);
            return p;
        }

        // UC01: Krok 2, Krok 3 
        private JPanel vytvorUc01NajdiKlientaPanel() {
            JPanel p = new JPanel(new BorderLayout());
            JLabel hl = new JLabel("Vyhľadanie klienta", SwingConstants.CENTER);
            hl.setFont(new Font("Segoe UI", Font.BOLD, 22));
            p.add(hl, BorderLayout.NORTH);

            JPanel center = new JPanel(new GridLayout(4, 1, 5, 5));
            center.setBorder(BorderFactory.createEmptyBorder(20, 160, 20, 160));

            JLabel popis = new JLabel(
                    "Vyhľadanie existujúceho klienta podľa mena a priezviska.",
                    SwingConstants.CENTER);
            popis.setFont(new Font("Segoe UI", Font.PLAIN, 18));
            center.add(popis);

            center.add(new JLabel("Zadajte meno a priezvisko klienta:", SwingConstants.CENTER));

            txtUc01MenoPriezvisko = new JTextField();
            center.add(txtUc01MenoPriezvisko);

            JPanel tl = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            JButton btnSpat = new JButton("Späť");
            styleSecondaryButton(btnSpat);
            JButton btnVyhladat = new JButton("Vyhľadať");
            stylePrimaryButton(btnVyhladat);

            btnVyhladat.addActionListener(e -> vyhladajKlienta());
            btnSpat.addActionListener(e -> prepnina(KARTA_DOMOV));

            tl.add(btnSpat);
            tl.add(btnVyhladat);
            center.add(tl);

            p.add(center, BorderLayout.CENTER);
            return p;
        }

        // UC01: Krok 4
        // metoda z mojich diagramov
        private void vyhladajKlienta() {
            
            String text = txtUc01MenoPriezvisko.getText().trim();
            
            if (text.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Zadajte meno a priezvisko.", "Chyba",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }
            String[] parts = text.split("\\s+");
            String meno = parts[0];
            String priezvisko = parts.length > 1 ? parts[1] : "";

            for (Klient k : klienti) {
                if (k.meno.equalsIgnoreCase(meno) &&
                        k.priezvisko.equalsIgnoreCase(priezvisko)) {
                    vybranyKlient = k;
                    lblUc01KlientOtazka.setText("Želáte si vytvoriť rezerváciu pre klienta: " + k.celeMeno() + " ?");
                    prepnina(KARTA_UC01_KLIENT_NAJDENY);
                    return;
                }
            }
            prepnina(KARTA_UC01_KLIENT_NENAJDENY);
        }

        // UC01: Krok 5 - system nasiel klienta a pyta sa na jeho potvrdenie 
        private JPanel vytvorUc01KlientNajdenyPanel() {
            JPanel p = new JPanel(new BorderLayout());
            JLabel hl = new JLabel("Klient nájdený", SwingConstants.CENTER);
            hl.setFont(new Font("Segoe UI", Font.BOLD, 22));
            p.add(hl, BorderLayout.NORTH);

            lblUc01KlientOtazka = new JLabel("", SwingConstants.CENTER);
            lblUc01KlientOtazka.setFont(new Font("Segoe UI", Font.PLAIN, 18));
            p.add(lblUc01KlientOtazka, BorderLayout.CENTER);

            JPanel tl = new JPanel(new FlowLayout(FlowLayout.CENTER));
            JButton btnNie = new JButton("Nie");
            styleSecondaryButton(btnNie);
            JButton btnAno = new JButton("Áno");
            stylePrimaryButton(btnAno);

            btnAno.addActionListener(e -> {
                inicializujUc01Termin();
                prepnina(KARTA_UC01_VYBER_TERMIN);
            });
            btnNie.addActionListener(e -> {
                vybranyKlient = null;
                prepnina(KARTA_DOMOV);
            });

            tl.add(btnNie);
            tl.add(btnAno);
            p.add(tl, BorderLayout.SOUTH);
            return p;
        }

        // UC01: Krok 5a1 - system oznamuje, ze klient neexistuje a ponuka registraciu
        // extend - UC02 z UC diagramu
        private JPanel vytvorUc01KlientNenajdenyPanel() {
            JPanel p = new JPanel(new BorderLayout());
            JLabel hl = new JLabel("Klient neexistuje", SwingConstants.CENTER);
            hl.setFont(new Font("Segoe UI", Font.BOLD, 22));
            p.add(hl, BorderLayout.NORTH);

            JLabel msg = new JLabel("Klient neexistuje. Je potrebné ho zaregistrovať.", SwingConstants.CENTER);
            msg.setFont(new Font("Segoe UI", Font.PLAIN, 18));
            p.add(msg, BorderLayout.CENTER);

            JPanel tl = new JPanel();
            JButton btnZrusit = new JButton("Zrušiť vytváranie rezervácie");
            styleSecondaryButton(btnZrusit);
            JButton btnReg = new JButton("Zaregistrovať klienta");
            stylePrimaryButton(btnReg);

            btnZrusit.addActionListener(e -> prepnina(KARTA_DOMOV));
            btnReg.addActionListener(e -> {
                poRegistraciiPokracovatVRezervacii = true;
                vynulujRegistraciu();
                prepnina(KARTA_UC02_REG_OSOBA_ADRESA);
            });

            tl.add(btnZrusit);
            tl.add(btnReg);
            p.add(tl, BorderLayout.SOUTH);
            return p;
        }

        private void inicializujUc01Termin() {
            spUc01Datum.setValue(new Date());
            cbUc01Cas.setSelectedIndex(0);
        }

        // UC01: Krok 6, Krok 7
        private JPanel vytvorUc01VyberTerminPanel() {
            JPanel p = new JPanel(new BorderLayout());
            JLabel hl = new JLabel("Výber dátumu a času", SwingConstants.CENTER);
            hl.setFont(new Font("Segoe UI", Font.BOLD, 22));
            p.add(hl, BorderLayout.NORTH);

            JPanel center = new JPanel(new GridLayout(4, 2, 5, 5));
            center.setBorder(BorderFactory.createEmptyBorder(20, 200, 20, 200));
            
            JLabel info = new JLabel(
                    "Vyberte dátum a čas rezervácie:",
                    SwingConstants.CENTER);
            info.setFont(new Font("Segoe UI", Font.PLAIN, 18));
            center.add(info);
            center.add(new JLabel(""));

            center.add(new JLabel("Dátum:", SwingConstants.RIGHT));

            spUc01Datum = new JSpinner(new SpinnerDateModel(new Date(), null, null, Calendar.DAY_OF_MONTH));
            JSpinner.DateEditor de = new JSpinner.DateEditor(spUc01Datum, "yyyy-MM-dd");
            spUc01Datum.setEditor(de);
            center.add(spUc01Datum);

            center.add(new JLabel("Čas:", SwingConstants.RIGHT));
            cbUc01Cas = new JComboBox<>();
            for (int h = 8; h <= 21; h++) {
                cbUc01Cas.addItem(String.format("%02d:00", h));
                cbUc01Cas.addItem(String.format("%02d:30", h));
            }
            center.add(cbUc01Cas);

            JButton btnSpat = new JButton("Späť");
            styleSecondaryButton(btnSpat);
            JButton btnDalej = new JButton("Pokračovať");
            stylePrimaryButton(btnDalej);

            btnSpat.addActionListener(e -> prepnina(KARTA_UC01_KLIENT_NAJDENY));
            btnDalej.addActionListener(e -> {
                if (vybranyKlient == null) {
                    JOptionPane.showMessageDialog(this, "Nie je vybraný klient.", "Chyba",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }
                taUc01Poznamka.setText("");
                prepnina(KARTA_UC01_POZNAMKA);
            });

            center.add(btnSpat);
            center.add(btnDalej);

            p.add(center, BorderLayout.CENTER);
            return p;
        }

        // UC01: Krok 8, Krok 9
        // metoda Poskytni zadanie poznamky() z mojich diagramov
        private JPanel vytvorUc01PoznamkaPanel() {
            JPanel p = new JPanel(new BorderLayout());

            JLabel hl = new JLabel("Zadanie poznámky", SwingConstants.CENTER);
            hl.setFont(new Font("Segoe UI", Font.BOLD, 22));

            JLabel info = new JLabel(
                    "Zadajte poznámku k rezervácii podľa požiadaviek klienta.",
                    SwingConstants.CENTER);
            info.setFont(new Font("Segoe UI", Font.PLAIN, 14));

            JPanel north = new JPanel(new GridLayout(2, 1));
            north.add(hl);
            north.add(info);
            p.add(north, BorderLayout.NORTH);

            taUc01Poznamka = new JTextArea(3, 30);
            taUc01Poznamka.setFont(new Font("Segoe UI", Font.PLAIN, 13));

            JScrollPane sp = new JScrollPane(taUc01Poznamka);
            sp.setPreferredSize(new java.awt.Dimension(400, 80)); 

            JPanel center = new JPanel(new FlowLayout(FlowLayout.CENTER));
            center.add(sp);
            p.add(center, BorderLayout.CENTER);

            JPanel tl = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            JButton btnSpat = new JButton("Späť");
            styleSecondaryButton(btnSpat);
            JButton btnDalej = new JButton("Pokračovať na sumarizáciu");
            stylePrimaryButton(btnDalej);

            btnSpat.addActionListener(e -> prepnina(KARTA_UC01_VYBER_TERMIN));
            btnDalej.addActionListener(e -> prechodNaSumar());

            tl.add(btnSpat);
            tl.add(btnDalej);
            p.add(tl, BorderLayout.SOUTH);

            return p;
        }

        // UC01: Krok 10 - system zobrazi sumarizaciu rezervacie
        private void prechodNaSumar() {
            Date d = (Date) spUc01Datum.getValue();
            LocalDate datum = LocalDate.ofInstant(d.toInstant(), ZoneId.systemDefault());
            LocalTime cas = LocalTime.parse((String) cbUc01Cas.getSelectedItem());
            String poznamka = taUc01Poznamka.getText();

            StringBuilder sb = new StringBuilder();
            sb.append("Klient: ").append(vybranyKlient.celeMeno()).append("\n");
            sb.append("Dátum: ").append(datum).append("\n");
            sb.append("Čas: ").append(cas).append("\n");
            sb.append("Poznámka: ").append(poznamka).append("\n");
            sb.append("Saunový majster: zatiaľ nepriradený\n");

            taUc01Sumar.setText(sb.toString());
            prepnina(KARTA_UC01_SUMAR);
        }

        // UC01: Krok 10, Krok 11 - zobrazenie sumarizacie rezervacie pred zavaznym vytvorenim
        // metody Zobraz sumarizaciu rezervacie(), Vyziadaj ulozenie rezervacie() z mojich diagramov
        private JPanel vytvorUc01SumarPanel() {
            JPanel p = new JPanel(new BorderLayout());
            JLabel hl = new JLabel("Sumarizácia rezervácie", SwingConstants.CENTER);
            hl.setFont(new Font("Segoe UI", Font.BOLD, 22));
            p.add(hl, BorderLayout.NORTH);

            taUc01Sumar = new JTextArea(10, 50);
            taUc01Sumar.setEditable(false);
            taUc01Sumar.setFont(new Font("Segoe UI", Font.PLAIN, 13));
            p.add(new JScrollPane(taUc01Sumar), BorderLayout.CENTER);

            JPanel tl = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            JButton btnSpat = new JButton("Späť");
            styleSecondaryButton(btnSpat);
            JButton btnZavazne = new JButton("Záväzne vytvoriť rezerváciu");
            stylePrimaryButton(btnZavazne);

            btnSpat.addActionListener(e -> prepnina(KARTA_UC01_POZNAMKA));
            btnZavazne.addActionListener(e -> zavazneVytvorRezervaciu());

            tl.add(btnSpat);
            tl.add(btnZavazne);
            p.add(tl, BorderLayout.SOUTH);
            return p;
        }

        // UC01: Krok 12, Krok 13 - overenie terminu a ulozenie rezervacie
        private void zavazneVytvorRezervaciu() {
            if (vybranyKlient == null) {
                JOptionPane.showMessageDialog(this, "Nie je vybraný klient.", "Chyba",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            Date d = (Date) spUc01Datum.getValue();
            LocalDate datum = LocalDate.ofInstant(d.toInstant(), ZoneId.systemDefault());
            LocalTime cas = LocalTime.parse((String) cbUc01Cas.getSelectedItem());

            // UC01: Krok 12a1
            if (terminObsadeny(datum, cas)) {
                JOptionPane.showMessageDialog(this,
                        "Požadovaný termín je obsadený. Zadajte iný dátum/čas.",
                        "Termín obsadený",
                        JOptionPane.WARNING_MESSAGE);
                prepnina(KARTA_UC01_VYBER_TERMIN);
                return;
            }

            String poznamka = taUc01Poznamka.getText();
            int noveId = rezervacie.stream().mapToInt(r -> r.technickeId).max().orElse(0) + 1;
            Rezervacia r = new Rezervacia(
                    noveId,
                    "R" + noveId,
                    vybranyKlient,
                    datum,
                    cas,
                    60,
                    poznamka
            );
            rezervacie.add(r);
            DatabazaSubory.ulozRezervacie(rezervacie);
            vybranaRezervacia = r;

            if (lblUc01VytvorenaText != null) {
                lblUc01VytvorenaText.setText("Rezervácia s ID " + r.technickeId + " bola záväzne vytvorená.");
            }

            prepnina(KARTA_UC01_VYTVORENA);
        }

        private boolean terminObsadeny(LocalDate datum, LocalTime cas) {
            for (Rezervacia r : rezervacie) {
                if (r.getDatum().equals(datum) && r.getCas().equals(cas)) {
                    return true;
                }
            }
            return false;
        }

        // UC01: Krok 14 - system potvrdi vytvorenie rezervacie a ponukne priradenie saunoveho majstra
        // include UC05 z UC diagramu - UC05 Prirad saunoveho majstra k rezervacii
        private JPanel vytvorUc01VytvorenaPanel() {
            JPanel p = new JPanel(new BorderLayout());
            JLabel hl = new JLabel("Rezervácia vytvorená", SwingConstants.CENTER);
            hl.setFont(new Font("Segoe UI", Font.BOLD, 22));
            p.add(hl, BorderLayout.NORTH);

            lblUc01VytvorenaText = new JLabel("Rezervácia bola záväzne vytvorená.", SwingConstants.CENTER);
            lblUc01VytvorenaText.setFont(new Font("Segoe UI", Font.PLAIN, 18));
            p.add(lblUc01VytvorenaText, BorderLayout.CENTER);

            JPanel tl = new JPanel();
            JButton btnPriradMajstra = new JButton("Priradiť saunového majstra");
            stylePrimaryButton(btnPriradMajstra);
            JButton btnNepriradit = new JButton("Nepriradiť saunového majstra a ukončiť vytváranie rezervácie");
            styleSecondaryButton(btnNepriradit);

            // UC05: Krok 1, Krok 2
            btnPriradMajstra.addActionListener(e -> {
                if (vybranaRezervacia != null) {
                    if (vybranaRezervacia.saunovyMajster != null) {
                        JOptionPane.showMessageDialog(this,
                                "K tejto rezervácii je už priradený saunový majster.",
                                "Informácia", JOptionPane.INFORMATION_MESSAGE);
                        return;
                    }
                    inicializujUc05PreRezervaciu();
                    prepnina(KARTA_UC05_VYBER_MAJSTRA);
                }
            });
            
            // UC05: Krok 2a1
            btnNepriradit.addActionListener(e -> {
                vybranaRezervacia = null;
                prepnina(KARTA_DOMOV);
            });

            tl.add(btnPriradMajstra);
            tl.add(btnNepriradit);
            p.add(tl, BorderLayout.SOUTH);
            return p;
        }

        private void vynulujRegistraciu() {
            regMeno = regPriezvisko = regUlica = regMesto = regPsc = regOrientacne = regSupisne = regStat = "";
            regTelefon = regEmail = regKontra = "";
            regVek = 0;

            if (txtRegMeno != null) txtRegMeno.setText("");
            if (txtRegPriezvisko != null) txtRegPriezvisko.setText("");
            if (spRegVek != null) spRegVek.setValue(30);
            if (txtRegUlica != null) txtRegUlica.setText("");
            if (txtRegMesto != null) txtRegMesto.setText("");
            if (txtRegPsc != null) txtRegPsc.setText("");
            if (txtRegOrientacne != null) txtRegOrientacne.setText("");
            if (txtRegSupisne != null) txtRegSupisne.setText("");
            if (txtRegStat != null) txtRegStat.setText("");
            if (taRegKontra != null) taRegKontra.setText("");
            if (txtRegTelefon != null) txtRegTelefon.setText("");
            if (txtRegEmail != null) txtRegEmail.setText("");
            if (taUc02RegSumar != null) taUc02RegSumar.setText("");
        }

        private boolean existujeKlientSIdentUdajmi(String meno, String priezvisko, int vek) {
            for (Klient k : klienti) {
                if (k.meno.equalsIgnoreCase(meno)
                        && k.priezvisko.equalsIgnoreCase(priezvisko)) {
                    return true;
                }
            }
            return false;
        }

        // UC02: Krok 4a1 - system overi, ci klient s rovnakymi identifikacnymi udajmi uz existuje
        private JPanel vytvorUc02KlientExistujePanel() {
            JPanel p = new JPanel(new BorderLayout());
            JLabel hl = new JLabel("Klient už existuje v systéme IntelliWell", SwingConstants.CENTER);
            hl.setFont(new Font("Segoe UI", Font.BOLD, 22));
            p.add(hl, BorderLayout.NORTH);

            JLabel msg = new JLabel(
                    "<html>Klient s týmito identifikačnými údajmi už existuje v systéme IntelliWell.<br>" +
                            "Nie je možné ho zaregistrovať znova.</html>",
                    SwingConstants.CENTER);
            msg.setFont(new Font("Segoe UI", Font.PLAIN, 16));
            p.add(msg, BorderLayout.CENTER);

            JPanel tl = new JPanel();
            JButton btnSpatNaReg = new JButton("Späť na identifikačné údaje");
            styleSecondaryButton(btnSpatNaReg);
            JButton btnDomov = new JButton("Späť na domovskú obrazovku");
            stylePrimaryButton(btnDomov);

            btnSpatNaReg.addActionListener(e -> prepnina(KARTA_UC02_REG_OSOBA_ADRESA));
            btnDomov.addActionListener(e -> prepnina(KARTA_DOMOV));

            tl.add(btnSpatNaReg);
            tl.add(btnDomov);
            p.add(tl, BorderLayout.SOUTH);
            return p;
        }

        // UC02: Krok 2, Krok 3
        // Klient podla class diagramu, dedi z osoby, ma adresu a kontakt
        // Adresa z class diagramu
        private JPanel vytvorUc02RegOsobaAdresaPanel() {
            JPanel p = new JPanel(new BorderLayout());
            JLabel hl = new JLabel("Registrácia klienta (1/4 - meno, priezvisko, vek, adresa)",
                    SwingConstants.CENTER);
            hl.setFont(new Font("Segoe UI", Font.BOLD, 22));
            p.add(hl, BorderLayout.NORTH);

            JPanel center = new JPanel(new GridLayout(9, 2, 5, 5));
            center.setBorder(BorderFactory.createEmptyBorder(20, 200, 20, 200));

            center.add(new JLabel("Meno (povinné):"));
            txtRegMeno = new JTextField();
            center.add(txtRegMeno);

            center.add(new JLabel("Priezvisko (povinné):"));
            txtRegPriezvisko = new JTextField();
            center.add(txtRegPriezvisko);

            center.add(new JLabel("Vek:"));
            spRegVek = new JSpinner(new SpinnerNumberModel(30, 0, 120, 1));
            center.add(spRegVek);

            center.add(new JLabel("Ulica:"));
            txtRegUlica = new JTextField();
            center.add(txtRegUlica);

            center.add(new JLabel("Mesto:"));
            txtRegMesto = new JTextField();
            center.add(txtRegMesto);

            center.add(new JLabel("PSČ:"));
            txtRegPsc = new JTextField();
            center.add(txtRegPsc);

            center.add(new JLabel("Orientačné číslo:"));
            txtRegOrientacne = new JTextField();
            center.add(txtRegOrientacne);

            center.add(new JLabel("Súpisné číslo:"));
            txtRegSupisne = new JTextField();
            center.add(txtRegSupisne);

            center.add(new JLabel("Štát:"));
            txtRegStat = new JTextField();
            center.add(txtRegStat);

            JPanel tl = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            JButton btnSpat = new JButton("Späť");
            styleSecondaryButton(btnSpat);
            JButton btnDalej = new JButton("Pokračovať (na 2/4)");
            stylePrimaryButton(btnDalej);

            btnSpat.addActionListener(e -> prepnina(KARTA_DOMOV));
            btnDalej.addActionListener(e -> ulozPrvyKrokRegistracie());

            tl.add(btnSpat);
            tl.add(btnDalej);

            p.add(center, BorderLayout.CENTER);
            p.add(tl, BorderLayout.SOUTH);

            return p;
        }

        private void ulozPrvyKrokRegistracie() {
            regMeno = txtRegMeno.getText().trim();
            regPriezvisko = txtRegPriezvisko.getText().trim();
            regVek = (Integer) spRegVek.getValue();
            regUlica = txtRegUlica.getText().trim();
            regMesto = txtRegMesto.getText().trim();
            regPsc = txtRegPsc.getText().trim();
            regOrientacne = txtRegOrientacne.getText().trim();
            regSupisne = txtRegSupisne.getText().trim();
            regStat = txtRegStat.getText().trim();

            if (regMeno.isEmpty() || regPriezvisko.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Meno a priezvisko sú povinné.", "Chyba",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // UC02: Krok 4
            if (existujeKlientSIdentUdajmi(regMeno, regPriezvisko, regVek)) {
                prepnina(KARTA_UC02_KLIENT_EXISTUJE);
                return;
            }

            prepnina(KARTA_UC02_REG_KONTAKT);
        }

        // UC02: Krok 4, Krok 5 
        // Kontakt z class diagramu
        private JPanel vytvorUc02RegKontaktPanel() {
            JPanel p = new JPanel(new BorderLayout());
            JLabel hl = new JLabel("Registrácia klienta (2/4 - kontakt)",
                    SwingConstants.CENTER);
            hl.setFont(new Font("Segoe UI", Font.BOLD, 22));
            p.add(hl, BorderLayout.NORTH);

            JPanel center = new JPanel(new GridLayout(3, 2, 5, 5));
            center.setBorder(BorderFactory.createEmptyBorder(20, 200, 20, 200));

            center.add(new JLabel("Telefónne číslo (povinné):"));
            txtRegTelefon = new JTextField();
            center.add(txtRegTelefon);

            center.add(new JLabel("E-mail (povinný):"));
            txtRegEmail = new JTextField();
            center.add(txtRegEmail);

            center.add(new JLabel(""));
            center.add(new JLabel(""));

            JPanel tl = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            JButton btnSpat = new JButton("Späť (na 1/4)");
            styleSecondaryButton(btnSpat);
            JButton btnDalej = new JButton("Pokračovať (na 3/4)");
            stylePrimaryButton(btnDalej);

            btnSpat.addActionListener(e -> prepnina(KARTA_UC02_REG_OSOBA_ADRESA));
            btnDalej.addActionListener(e -> ulozKontaktPreUc02());

            tl.add(btnSpat);
            tl.add(btnDalej);

            p.add(center, BorderLayout.CENTER);
            p.add(tl, BorderLayout.SOUTH);
            return p;
        }

        private void ulozKontaktPreUc02() {
            regTelefon = txtRegTelefon.getText().trim();
            regEmail = txtRegEmail.getText().trim();

            if (regTelefon.isEmpty() || regEmail.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "Telefónne číslo aj e-mail sú povinné.",
                        "Chyba",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }
            prepnina(KARTA_UC02_REG_KONTRA);
        }

        // UC02: Krok 6, Krok 7
        // Kontraindikacie z class diagramu 
        private JPanel vytvorUc02RegKontraPanel() {
            JPanel p = new JPanel(new BorderLayout());
            JLabel hl = new JLabel("Registrácia klienta (3/4 - kontraindikácie)",
                    SwingConstants.CENTER);
            hl.setFont(new Font("Segoe UI", Font.BOLD, 22));
            p.add(hl, BorderLayout.NORTH);

            JPanel center = new JPanel(new BorderLayout());
            JLabel l = new JLabel("Zadajte popis kontraindikácií klienta:", SwingConstants.CENTER);
            l.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            center.add(l, BorderLayout.NORTH);

            taRegKontra = new JTextArea(3, 30);
            taRegKontra.setFont(new Font("Segoe UI", Font.PLAIN, 13));

            JScrollPane sp = new JScrollPane(taRegKontra);
            sp.setPreferredSize(new java.awt.Dimension(400, 80));

            JPanel wrapper = new JPanel(new FlowLayout(FlowLayout.CENTER));
            wrapper.add(sp);

            center.add(wrapper, BorderLayout.CENTER);

            JPanel tl = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            JButton btnSpat = new JButton("Späť (na 2/4)");
            styleSecondaryButton(btnSpat);
            JButton btnDalej = new JButton("Pokračovať na sumarizáciu (na 4/4)");
            stylePrimaryButton(btnDalej);

            btnSpat.addActionListener(e -> prepnina(KARTA_UC02_REG_KONTAKT));
            btnDalej.addActionListener(e -> {
                regKontra = taRegKontra.getText().trim();
                prechodNaUc02Sumar();
            });

            tl.add(btnSpat);
            tl.add(btnDalej);

            p.add(center, BorderLayout.CENTER);
            p.add(tl, BorderLayout.SOUTH);
            return p;
        }


        private void prechodNaUc02Sumar() {
            // UC02: Krok 8 - sumarizacia zaevidovanych udajov klienta
            regKontra = taRegKontra != null ? taRegKontra.getText().trim() : regKontra;

            StringBuilder sb = new StringBuilder();
            sb.append("Meno: ").append(regMeno).append("\n");
            sb.append("Priezvisko: ").append(regPriezvisko).append("\n");
            sb.append("Vek: ").append(regVek).append("\n");
            sb.append("Adresa: ").append(regUlica).append(", ").append(regMesto).append(" ")
                    .append(regPsc).append(", ").append(regOrientacne).append("/").append(regSupisne)
                    .append(", ").append(regStat).append("\n");
            sb.append("Kontraindikácie: ").append(regKontra).append("\n");
            sb.append("Telefón: ").append(regTelefon).append("\n");
            sb.append("E-mail: ").append(regEmail).append("\n");

            taUc02RegSumar.setText(sb.toString());

            prepnina(KARTA_UC02_REG_SUMAR);
        }

        // UC02: Krok 8, Krok 9 - sumarizacia a zavazne zaregistrovanie klienta
        // metoda zo Spravca klientov - Zaregistruj klienta(), mam ju v sekvencnych diagramoch aj v diagrame aktivit
        private JPanel vytvorUc02RegSumarPanel() {
            JPanel p = new JPanel(new BorderLayout());
            JLabel hl = new JLabel("Sumarizácia údajov klienta (4/4)",
                    SwingConstants.CENTER);
            hl.setFont(new Font("Segoe UI", Font.BOLD, 22));
            p.add(hl, BorderLayout.NORTH);

            taUc02RegSumar = new JTextArea(10, 50);
            taUc02RegSumar.setEditable(false);
            taUc02RegSumar.setFont(new Font("Segoe UI", Font.PLAIN, 13));
            p.add(new JScrollPane(taUc02RegSumar), BorderLayout.CENTER);

            JPanel tl = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            JButton btnSpat = new JButton("Späť (na 3/4)");
            styleSecondaryButton(btnSpat);
            JButton btnUlozit = new JButton("Záväzne zaregistrovať klienta");
            stylePrimaryButton(btnUlozit);

            btnSpat.addActionListener(e -> prepnina(KARTA_UC02_REG_KONTRA));
            btnUlozit.addActionListener(e -> ulozKlientaUc02());

            tl.add(btnSpat);
            tl.add(btnUlozit);

            p.add(tl, BorderLayout.SOUTH);
            return p;
        }

        // UC02: Krok 10 - system vytvori noveho klienta a ulozi ho do evidencie
        private void ulozKlientaUc02() {
            regTelefon = txtRegTelefon.getText().trim();
            regEmail = txtRegEmail.getText().trim();
            regKontra = taRegKontra.getText().trim();

            if (regTelefon.isEmpty() || regEmail.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "Telefónne číslo aj e-mail sú povinné.",
                        "Chyba",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            if (existujeKlientSIdentUdajmi(regMeno, regPriezvisko, regVek)) {
                prepnina(KARTA_UC02_KLIENT_EXISTUJE);
                return;
            }

            int noveId = klienti.stream().mapToInt(k -> k.technickeId).max().orElse(0) + 1;

            Klient k = new Klient();
            k.technickeId = noveId;
            k.identifikator = String.valueOf(noveId);
            k.meno = regMeno;
            k.priezvisko = regPriezvisko;
            k.vek = regVek;

            k.adresa = new Adresa();
            k.adresa.ulica = regUlica;
            k.adresa.mesto = regMesto;
            k.adresa.psc = regPsc;
            k.adresa.orientacneCislo = regOrientacne;
            k.adresa.supisneCislo = regSupisne;
            k.adresa.stat = regStat;

            k.kontakt = new Kontakt();
            k.kontakt.telefonneCislo = regTelefon;
            k.kontakt.email = regEmail;

            Kontraindikacie kontra = new Kontraindikacie();
            kontra.popisKontraindikacii = regKontra;
            k.kontraindikacie = kontra;

            klienti.add(k);
            DatabazaSubory.ulozKlientov(klienti);
            vybranyKlient = k;

            JOptionPane.showMessageDialog(this,
                    "Klient bol zaregistrovaný: " + k.celeMeno(),
                    "Informácia", JOptionPane.INFORMATION_MESSAGE);

            if (poRegistraciiPokracovatVRezervacii) {
                inicializujUc01Termin();
                prepnina(KARTA_UC01_VYBER_TERMIN);
            } else {
                prepnina(KARTA_DOMOV);
            }
        }

        // UC05: Krok 1a2, Krok 1a3 - vyhladanie rezervacie podla ID
        // metoda Vyhladaj rezervaciu v existujucej evidencii rezervacii() z mojho class diagramu - Spravca rezervacii, ale aj zo sekvencneho diagramu a diagramu aktivit
        private JPanel vytvorUc02VyhladanieRezPanel() {
            JPanel p = new JPanel(new BorderLayout());
            JLabel hl = new JLabel("Vyhľadanie rezervácie podľa ID", SwingConstants.CENTER);
            hl.setFont(new Font("Segoe UI", Font.BOLD, 22));
            p.add(hl, BorderLayout.NORTH);

            JPanel center = new JPanel(new GridLayout(3, 1, 5, 5));
            center.setBorder(BorderFactory.createEmptyBorder(20, 200, 20, 200));
            center.add(new JLabel("Zadajte ID rezervácie:", SwingConstants.CENTER));

            txtUc05VyhlId = new JTextField();
            center.add(txtUc05VyhlId);

            JPanel tl = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            JButton btnSpat = new JButton("Späť");
            styleSecondaryButton(btnSpat);
            JButton btnVyhladat = new JButton("Vyhľadať");
            stylePrimaryButton(btnVyhladat);

            btnSpat.addActionListener(e -> prepnina(KARTA_DOMOV));
            btnVyhladat.addActionListener(e -> vyhladajRezervaciuPodlaId());

            tl.add(btnSpat);
            tl.add(btnVyhladat);
            center.add(tl);

            p.add(center, BorderLayout.CENTER);
            return p;
        }
        
        // UC05: Krok 1a3 - system vyhlada rezervaciu v evidencii podla zadaneho ID
        private void vyhladajRezervaciuPodlaId() {
            String sId = txtUc05VyhlId.getText().trim();
            if (sId.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Zadajte ID.", "Chyba", JOptionPane.ERROR_MESSAGE);
                return;
            }
            try {
                int id = Integer.parseInt(sId);
                for (Rezervacia r : rezervacie) {
                    if (r.technickeId == id) {
                        vybranaRezervacia = r;
                        zobrazDetailRezervacie();
                        prepnina(KARTA_UC05_DETAIL_REZ);
                        return;
                    }
                }
                JOptionPane.showMessageDialog(this, "Rezervácia s týmto ID neexistuje.", "Informácia",
                        JOptionPane.INFORMATION_MESSAGE);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "ID musí byť číslo.", "Chyba",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
        
        // UC05: Krok 1a4
        // UC03	: Krok 4
        private String buildRezervaciaDetail(Rezervacia r) {
            if (r == null) return "";

            StringBuilder sb = new StringBuilder();
            sb.append("<html><body style='font-family:Segoe UI; font-size:13px;'>");

            sb.append("<b><span style='color:#003366;'>ID:</span></b> ")
                    .append(r.technickeId).append("<br>");
            sb.append("<b><span style='color:#003366;'>Identifikátor:</span></b> ")
                    .append(r.identifikator).append("<br>");
            sb.append("<b><span style='color:#003366;'>Stav rezervácie:</span></b> ")
                    .append(r.stav).append("<br>");
            sb.append("<b><span style='color:#003366;'>Dátum:</span></b> ")
                    .append(r.getDatum()).append("<br>");
            sb.append("<b><span style='color:#003366;'>Čas:</span></b> ")
                    .append(r.getCas()).append("<br>");
            sb.append("<b><span style='color:#003366;'>Poznámka:</span></b> ")
                    .append(r.poznamka == null ? "" : r.poznamka).append("<br><br>");

            if (r.klient != null) {
                sb.append("<b><span style='color:#003366;'>Klient:</span></b> ")
                        .append(r.klient.celeMeno()).append("<br>");
                sb.append("<b><span style='color:#003366;'>Vek:</span></b> ")
                        .append(r.klient.vek).append("<br>");

                if (r.klient.kontakt != null) {
                    sb.append("<b><span style='color:#003366;'>Telefón:</span></b> ")
                            .append(r.klient.kontakt.telefonneCislo).append("<br>");
                    sb.append("<b><span style='color:#003366;'>E-mail:</span></b> ")
                            .append(r.klient.kontakt.email).append("<br>");
                }
                if (r.klient.adresa != null) {
                    sb.append("<b><span style='color:#003366;'>Adresa:</span></b> ")
                            .append(r.klient.adresa.ulica).append(" ")
                            .append(r.klient.adresa.orientacneCislo).append("/")
                            .append(r.klient.adresa.supisneCislo).append(", ")
                            .append(r.klient.adresa.psc).append(" ")
                            .append(r.klient.adresa.mesto).append(", ")
                            .append(r.klient.adresa.stat).append("<br>");
                }
                if (r.klient.kontraindikacie != null) {
                    sb.append("<b><span style='color:#003366;'>Kontraindikácie:</span></b> ")
                            .append(r.klient.kontraindikacie.popisKontraindikacii)
                            .append("<br>");
                }
            }

            sb.append("<br><b><span style='color:#990000;'>PARAMETRE PROSTREDIA</span></b><br>");
            if (maRezervaciaNastaveneParametre(r)) {
                sb.append("<b><span style='color:#003366;'>Teplota vírivky:</span></b> ")
                        .append(r.parameter.pocetStupnovVoVirivke).append(" °C<br>");
                sb.append("<b><span style='color:#003366;'>Teplota sauny:</span></b> ")
                        .append(r.parameter.pocetStupnovVSaune).append(" °C<br>");
                sb.append("<b><span style='color:#003366;'>Teplota spoločného priestoru:</span></b> ")
                        .append(r.parameter.pocetStupnovVSpolocnomPriestore).append(" °C<br>");
            } else {
                sb.append("Parametre zatiaľ neboli nastavené.<br>");
            }

            sb.append("<br><b><span style='color:#003366;'>Profil:</span></b> ")
                    .append(r.profil != null ? r.profil.nazov : "nezvolený").append("<br>");
            sb.append("<b><span style='color:#003366;'>Saunový majster:</span></b> ")
                    .append(r.saunovyMajster != null ? r.saunovyMajster.meno : "nepriradený")
                    .append("<br>");
            sb.append("<b><span style='color:#003366;'>Aróma:</span></b> ")
                    .append(r.aroma != null ? r.aroma.nazov : "nevybraná").append("<br>");
            sb.append("<b><span style='color:#003366;'>Typ starostlivosti o pokožku:</span></b> ")
                    .append(r.starostlivostOPokozku != null ? r.starostlivostOPokozku.nazov : "nevybraný")
                    .append("<br>");

            sb.append("<b><span style='color:#003366;'>Bol požadovaný saunový majster:</span></b> ")
                    .append(r.bolVyzadovanySaunovyMajster ? "áno" : "nie").append("<br>");
            sb.append("<b><span style='color:#003366;'>Stav vyjadrenia saunového majstra:</span></b> ")
                    .append(r.stavVyjadreniaSaunovehoMajstra != null
                            ? r.stavVyjadreniaSaunovehoMajstra
                            : "nezadaný")
                    .append("<br>");

            if (r.datumUlozeniaRezervacie != null) {
                sb.append("<b><span style='color:#003366;'>Dátum uloženia rezervácie:</span></b> ")
                        .append(r.datumUlozeniaRezervacie).append("<br>");
            }
            if (r.datumExpiracie != null) {
                sb.append("<b><span style='color:#003366;'>Dátum expirácie:</span></b> ")
                        .append(r.datumExpiracie).append("<br>");
            }
            if (r.datumZruseniaRezervacie != null) {
                sb.append("<b><span style='color:#003366;'>Dátum zrušenia rezervácie:</span></b> ")
                        .append(r.datumZruseniaRezervacie).append("<br>");
            }

            sb.append("</body></html>");
            return sb.toString();
        }
 
        // UC05: Krok 1a4, Krok 1a5, Krok 1 - zobrazenie detailu rezervacie a moznost priradenia saunoveho majstra
        private JPanel vytvorUc02DetailRezPanel() {
            JPanel p = new JPanel(new BorderLayout());
            JLabel hl = new JLabel("Detail rezervácie", SwingConstants.CENTER);
            hl.setFont(new Font("Segoe UI", Font.BOLD, 22));
            p.add(hl, BorderLayout.NORTH);
         
            taUc02DetailRez = new JEditorPane("text/html", "");
            taUc02DetailRez.setEditable(false);
            taUc02DetailRez.setBackground(new Color(250, 250, 240));
            p.add(new JScrollPane(taUc02DetailRez), BorderLayout.CENTER);

            JPanel tl = new JPanel(new FlowLayout(FlowLayout.CENTER));
            JButton btnSpat = new JButton("Späť");
            styleSecondaryButton(btnSpat);
            JButton btnPriradMajstra = new JButton("Priradiť saunového majstra");
            stylePrimaryButton(btnPriradMajstra);

            btnSpat.addActionListener(e -> prepnina(KARTA_UC05_VYHLADANIE_REZ));
            btnPriradMajstra.addActionListener(e -> {
                if (vybranaRezervacia != null) {
                    if (vybranaRezervacia.saunovyMajster != null) {
                        JOptionPane.showMessageDialog(this,
                                "K tejto rezervácii už je priradený saunový majster.",
                                "Informácia",
                                JOptionPane.INFORMATION_MESSAGE);
                        return;
                    }
                    inicializujUc05PreRezervaciu();
                    prepnina(KARTA_UC05_VYBER_MAJSTRA);
                }
            });

            tl.add(btnSpat);
            tl.add(btnPriradMajstra);
            p.add(tl, BorderLayout.SOUTH);
            return p;
        }

        private void zobrazDetailRezervacie() {
            if (vybranaRezervacia == null) return;
            taUc02DetailRez.setText(buildRezervaciaDetail(vybranaRezervacia));
        }

        private void inicializujUc05PreRezervaciu() {
            if (cbUc05Majster != null) {
                cbUc05Majster.removeAllItems();
                cbUc05Majster.addItem(jedinyMajster);
            }
        }

        // UC05: Krok 3, Krok 4 - vyber saunoveho majstra
        // metoda Vyber saunoveho majstra() z classy Spravca saunoveho majstra a z mojho diagramu aktivit a mojho sekvencneho diagramu
        private JPanel vytvorUc05VyberMajstraPanel() {
            JPanel p = new JPanel(new BorderLayout());
            JLabel hl = new JLabel("Výber saunového majstra", SwingConstants.CENTER);
            hl.setFont(new Font("Segoe UI", Font.BOLD, 22));
            p.add(hl, BorderLayout.NORTH);

            JPanel center = new JPanel(new GridLayout(3, 1, 5, 5));
            center.setBorder(BorderFactory.createEmptyBorder(20, 200, 20, 200));
            center.add(new JLabel("Zvoľte saunového majstra:", SwingConstants.CENTER));

            cbUc05Majster = new JComboBox<>();
            center.add(cbUc05Majster);

            JLabel info = new JLabel(
                    "Po výbere sa overí dostupnosť saunového majstra v zvolenom termíne rezervácie.",
                    SwingConstants.CENTER);
            info.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            center.add(info);

            JPanel tl = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            JButton btnZrusit = new JButton("Zrušiť");
            styleSecondaryButton(btnZrusit);
            JButton btnVybrat = new JButton("Vybrať");
            stylePrimaryButton(btnVybrat);

            btnZrusit.addActionListener(e -> {
                vybranaRezervacia = null;
                prepnina(KARTA_DOMOV);
            });
            btnVybrat.addActionListener(e -> overDostupnostSaunovehoMajstra());

            tl.add(btnZrusit);
            tl.add(btnVybrat);

            p.add(center, BorderLayout.CENTER);
            p.add(tl, BorderLayout.SOUTH);
            return p;
        }

        // UC05: Krok 5 - system overi dostupnost saunoveho majstra 
        // metoda Over dostupnost saunoveho majstra() z class diagramu z classy Spravca saunoveho majstra a z mojho sekvencneho diagramu a mojho diagramu aktivit
        private void overDostupnostSaunovehoMajstra() {
            if (vybranaRezervacia == null) {
                JOptionPane.showMessageDialog(this, "Nie je vybraná rezervácia.", "Chyba",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }
            SaunovyMajster majster = (SaunovyMajster) cbUc05Majster.getSelectedItem();
            if (majster == null) return;

            boolean dostupny = overDostupnostTerminu(majster, vybranaRezervacia);

            if (!dostupny) {
                prepnina(KARTA_UC05_MAJSTER_NEDOSTUPNY);
            } else {
                lblUc05DostupnostOtazka.setText(
                        "Saunový majster " + majster.meno + " je dostupný. Želáte si ho potvrdiť?");
                prepnina(KARTA_UC05_MAJSTER_DOSTUPNY);
            }
        }

        // metoda Over dostupnost terminu() z mojho sekvencneho diagramu a class diagramu - class Spravca rezervacii
        private boolean overDostupnostTerminu(SaunovyMajster majster, Rezervacia rez) {
            if (rez.getCas().equals(LocalTime.of(18, 0))) {
                return false;
            }

            for (Rezervacia r : rezervacie) {
                if (r.saunovyMajster != null &&
                        r.saunovyMajster.technickeId == majster.technickeId) {
                    boolean overlap = !(rez.getKoniec().isBefore(r.datumACasZaciatku) ||
                            rez.datumACasZaciatku.isAfter(r.getKoniec()));
                    if (overlap && r.technickeId != rez.technickeId) {
                        return false;
                    }
                }
            }
            return true;
        }

        // UC05: Krok 5a1 - saunovy majster je nedostupny
        // metoda Zobraz informaciu o nedostupnosti saunoveho majstra() z mojho class diagramu - class Spravca rezervacii a z mojho sekvencneho diagramu a diagramu aktivit
        private JPanel vytvorUc05MajsterNedostupnyPanel() {
            JPanel p = new JPanel(new BorderLayout());
            JLabel hl = new JLabel("Saunový majster je nedostupný", SwingConstants.CENTER);
            hl.setFont(new Font("Segoe UI", Font.BOLD, 22));
            p.add(hl, BorderLayout.NORTH);

            JLabel msg = new JLabel("<html>Saunový majster nie je dostupný v požadovanom termíne.<br>" +
                    "Systém vás vráti na domovskú obrazovku.</html>", SwingConstants.CENTER);
            msg.setFont(new Font("Segoe UI", Font.PLAIN, 16));
            p.add(msg, BorderLayout.CENTER);

            JButton btnOk = new JButton("OK");
            stylePrimaryButton(btnOk);
            btnOk.addActionListener(e -> {
                vybranaRezervacia = null;
                prepnina(KARTA_DOMOV);
            });

            JPanel tl = new JPanel();
            tl.add(btnOk);
            p.add(tl, BorderLayout.SOUTH);
            return p;
        }

        // UC05: Krok 6 - system informuje o dostupnosti saunoveho majstra a pyta sa na jeho potvrdenie
        // metoda Zvol priradenie saunoveho majstra k rezervacii() z class diagramu - class Spravca saunoveho majstra, z mojho sekvencneho diagramu a mojho diagramu aktivit
        private JPanel vytvorUc05MajsterDostupnyPanel() {
            JPanel p = new JPanel(new BorderLayout());
            JLabel hl = new JLabel("Saunový majster je dostupný", SwingConstants.CENTER);
            hl.setFont(new Font("Segoe UI", Font.BOLD, 22));
            p.add(hl, BorderLayout.NORTH);

            lblUc05DostupnostOtazka = new JLabel("", SwingConstants.CENTER);
            lblUc05DostupnostOtazka.setFont(new Font("Segoe UI", Font.PLAIN, 18));
            p.add(lblUc05DostupnostOtazka, BorderLayout.CENTER);

            JPanel tl = new JPanel();
            JButton btnNie = new JButton("Nie");
            styleSecondaryButton(btnNie);
            JButton btnAno = new JButton("Áno");
            stylePrimaryButton(btnAno);

            btnNie.addActionListener(e -> {
                vybranaRezervacia = null;
                prepnina(KARTA_DOMOV);
            });
            btnAno.addActionListener(e -> prepnina(KARTA_UC05_VYBER_AROMY));

            tl.add(btnNie);
            tl.add(btnAno);
            p.add(tl, BorderLayout.SOUTH);
            return p;
        }

        // UC05: Krok 7, Krok 8 
        // metoda Ponukni vyber typu aromy() z class diagramu - class Spravca rezervacii, z mojho diagramu aktivit a mojho sekvencneho diagramu
        private JPanel vytvorUc05VyberAromyPanel() {
            JPanel p = new JPanel(new BorderLayout());
            JLabel hl = new JLabel("Výber arómy", SwingConstants.CENTER);
            hl.setFont(new Font("Segoe UI", Font.BOLD, 22));
            p.add(hl, BorderLayout.NORTH);

            JPanel center = new JPanel(new GridLayout(3, 1, 5, 5));
            center.setBorder(BorderFactory.createEmptyBorder(20, 200, 20, 200));
            center.add(new JLabel("Vyberte typ arómy:", SwingConstants.CENTER));

            cbUc05Aroma = new JComboBox<>();
            cbUc05Aroma.addItem("Citrusová");
            cbUc05Aroma.addItem("Mentolová");
            cbUc05Aroma.addItem("Bylinková");
            cbUc05Aroma.addItem("Levanduľová");
            center.add(cbUc05Aroma);

            JLabel info = new JLabel("Zvolená aróma bude súčasťou rituálu so saunovým majstrom.", SwingConstants.CENTER);
            info.setFont(new Font("Segoe UI", Font.PLAIN, 13));
            center.add(info);

            JPanel tl = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            JButton btnSpat = new JButton("Späť");
            styleSecondaryButton(btnSpat);
            JButton btnDalej = new JButton("Pokračovať");
            stylePrimaryButton(btnDalej);

            btnSpat.addActionListener(e -> prepnina(KARTA_UC05_MAJSTER_DOSTUPNY));
            btnDalej.addActionListener(e -> prepnina(KARTA_UC05_VYBER_STAROSTLIVOSTI));

            tl.add(btnSpat);
            tl.add(btnDalej);

            p.add(center, BorderLayout.CENTER);
            p.add(tl, BorderLayout.SOUTH);
            return p;
        }

        // UC05: Krok 9, krok 10
        // metoda Ponukni vyber typu starostlivosti o pokozku() z class diagramu - class Spravca rezervacii, z mojho diagramu aktivit a mojho sekvencneho diagramu
        private JPanel vytvorUc05VyberStarostlivostiPanel() {
            JPanel p = new JPanel(new BorderLayout());
            JLabel hl = new JLabel("Výber starostlivosti o pokožku", SwingConstants.CENTER);
            hl.setFont(new Font("Segoe UI", Font.BOLD, 22));
            p.add(hl, BorderLayout.NORTH);

            JPanel center = new JPanel(new GridLayout(3, 1, 5, 5));
            center.setBorder(BorderFactory.createEmptyBorder(20, 200, 20, 200));
            center.add(new JLabel("Vyberte typ starostlivosti o pokožku:", SwingConstants.CENTER));

            cbUc05Star = new JComboBox<>();
            cbUc05Star.addItem("Peeling");
            cbUc05Star.addItem("Medová");
            cbUc05Star.addItem("Olejová");
            cbUc05Star.addItem("Bylinková");
            center.add(cbUc05Star);

            JLabel info = new JLabel(
                    "Zvolená starostlivosť o pokožku bude súčasťou rituálu so saunovým majstrom.",
                    SwingConstants.CENTER);
            info.setFont(new Font("Segoe UI", Font.PLAIN, 13));
            center.add(info);

            JPanel tl = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            JButton btnSpat = new JButton("Späť");
            styleSecondaryButton(btnSpat);
            JButton btnOdoslat = new JButton("Odoslať požiadavku na saunového majstra");
            stylePrimaryButton(btnOdoslat);

            btnSpat.addActionListener(e -> prepnina(KARTA_UC05_VYBER_AROMY));
            btnOdoslat.addActionListener(e -> odosliPoziadavkuNaSaunovehoMajstra());

            tl.add(btnSpat);
            tl.add(btnOdoslat);

            p.add(center, BorderLayout.CENTER);
            p.add(tl, BorderLayout.SOUTH);
            return p;
        }

        // UC05: Krok 11 - odoslanie poziadavky na saunoveho majstra, moja metoda z class diagramu - class Spravca saunoveho majstra, mojho sekvencneho diagramu a diagramu aktivit
        private void odosliPoziadavkuNaSaunovehoMajstra() {
            if (vybranaRezervacia == null) {
                JOptionPane.showMessageDialog(this, "Nie je vybraná rezervácia.", "Chyba",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }
            Aroma a = new Aroma();
            a.nazov = (String) cbUc05Aroma.getSelectedItem();
            vybranaRezervacia.zapamatajSiVybranyTypAromy(a);

            StarostlivostOPokozku s = new StarostlivostOPokozku();
            s.nazov = (String) cbUc05Star.getSelectedItem();
            vybranaRezervacia.zapamatajSiVybranyTypStarostlivostiOPokozku(s);

            prepnina(KARTA_UC05_CAKANIE_SCHVALENIE);
        }

        // UC05: Krok 12, Krok 13 - cakanie na schvalenie saunovym majstrom
        // metody Spracuj poziadavku() a Zaeviduj priradenie saunoveho majstra k rezervacii() z mojich diagramov - aktivit, sekvencneho a class
        private JPanel vytvorUc05CakanieSchvaleniePanel() {
            JPanel p = new JPanel(new BorderLayout());
            JLabel hl = new JLabel("Čakanie na schválenie saunovým majstrom", SwingConstants.CENTER);
            hl.setFont(new Font("Segoe UI", Font.BOLD, 22));
            p.add(hl, BorderLayout.NORTH);

            lblUc05CakanieText = new JLabel(
                    "Prebieha schvaľovanie požiadavky saunovým majstrom...",
                    SwingConstants.CENTER);
            lblUc05CakanieText.setFont(new Font("Segoe UI", Font.PLAIN, 16));
            p.add(lblUc05CakanieText, BorderLayout.CENTER);

            JPanel tl = new JPanel(new FlowLayout(FlowLayout.CENTER));
            btnUc05CakanieOk = new JButton("OK");
            stylePrimaryButton(btnUc05CakanieOk);
            btnUc05CakanieOk.setVisible(false);
            tl.add(btnUc05CakanieOk);
            p.add(tl, BorderLayout.SOUTH);

            p.addComponentListener(new ComponentAdapter() {
                @Override
                public void componentShown(ComponentEvent e) {
                    btnUc05CakanieOk.setVisible(false);
                    lblUc05CakanieText.setText(
                            "Prebieha schvaľovanie požiadavky saunovým majstrom...");
                     
                    javax.swing.Timer t = new javax.swing.Timer(10000, ev -> {
                        if (vybranaRezervacia != null) {
                            jedinyMajster.spracujPoziadavku();

                            pocetPoziadaviekNaSaunovehoMajstra++;
                            boolean schvali = (pocetPoziadaviekNaSaunovehoMajstra % 2 == 1);

                            vybranaRezervacia.bolVyzadovanySaunovyMajster = true;

                            if (schvali) { 
                                vybranaRezervacia.saunovyMajster = jedinyMajster;
                                vybranaRezervacia.stavVyjadreniaSaunovehoMajstra =
                                        StavVyjadreniaSaunovehoMajstra.SCHVALENA_SAUNOVYM_MAJSTROM;

                                lblUc05CakanieText.setText(
                                        "<html>Saunový majster schválil priradenie k rezervácii.<br>" +
                                                "Saunový majster je priradený k rezervácii.</html>");
                            } else {
                                // UC05: Krok 12a1
                                vybranaRezervacia.saunovyMajster = null; 
                                vybranaRezervacia.stavVyjadreniaSaunovehoMajstra =
                                        StavVyjadreniaSaunovehoMajstra.ZAMIETNUTA_SAUNOVYM_MAJSTROM;

                                lblUc05CakanieText.setText(
                                        "<html>Saunový majster zamietol priradenie k rezervácii.<br>" +
                                                "Saunový majster nie je priradený k rezervácii.</html>");
                            }

                            vybranaRezervacia.stav = StavRezervacie.VYTVORENA;
                            DatabazaSubory.ulozRezervacie(rezervacie);
                        }

                        btnUc05CakanieOk.setVisible(true);
                    });
 
                    t.setRepeats(false);
                    t.start();
                }
            });
         
            btnUc05CakanieOk.addActionListener(e -> {
                vybranaRezervacia = null;
                prepnina(KARTA_DOMOV);
            });

            return p;
        }

        
        // UC03: Krok 1, Krok 2 - zobrazenie zoznamu rezervacii 
        // metoda Vyber zobrazenie vsetkych rezervacii v aktualny den() z class diagramu - class Spravca rezervacii a diagramu aktivit a sekvencneho diagramu
        void obnovZoznamRezervaciiPreUc03() {
            if (modelUc03Rezervacie == null) return;
            modelUc03Rezervacie.clear();
            for (Rezervacia r : rezervacie) {
                modelUc03Rezervacie.addElement(r);
            }
        }

        private JPanel vytvorUc03ZoznamRezPanel() {
            JPanel p = new JPanel(new BorderLayout());
            p.setBackground(new Color(245, 248, 255));

            JLabel hl = new JLabel("Výber rezervácie (pre prípravu prostredia)", SwingConstants.CENTER);
            hl.setFont(new Font("Segoe UI", Font.BOLD, 22));

            JLabel popis = new JLabel(
                    "Vyberte rezerváciu, pre ktorú chcete spustiť prípravu prostredia NikaWell-u.",
                    SwingConstants.CENTER);
            popis.setFont(new Font("Segoe UI", Font.PLAIN, 14));

            JPanel top = new JPanel(new GridLayout(2, 1));
            top.setBackground(new Color(245, 248, 255));
            top.add(hl);
            top.add(popis);
            p.add(top, BorderLayout.NORTH);

            modelUc03Rezervacie = new DefaultListModel<>();
            listUc03Rezervacie = new JList<>(modelUc03Rezervacie);
            listUc03Rezervacie.setFont(new Font("Segoe UI", Font.PLAIN, 13));
            listUc03Rezervacie.setBorder(BorderFactory.createTitledBorder("Rezervácie"));
            p.add(new JScrollPane(listUc03Rezervacie), BorderLayout.CENTER);

            JPanel tl = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            JButton btnSpat = new JButton("Späť");
            styleSecondaryButton(btnSpat);
            JButton btnZacat = new JButton("Začať prípravu prostredia NikaWell-u");
            stylePrimaryButton(btnZacat);

            btnSpat.addActionListener(e -> prepnina(KARTA_DOMOV));
            btnZacat.addActionListener(e -> zacniPripravuProstrediaNikaWellu());

            tl.add(btnSpat);
            tl.add(btnZacat);
            p.add(tl, BorderLayout.SOUTH);
            return p;
        }

        private boolean maRezervaciaNastaveneParametre(Rezervacia r) {
            if (r == null || r.parameter == null) {
                return false;
            }

            return !(r.parameter.pocetStupnovVoVirivke == 0
                    && r.parameter.pocetStupnovVSaune == 0
                    && r.parameter.pocetStupnovVSpolocnomPriestore == 0);
        }

        
        	// UC03: Krok 3 - recepcna vyberie rezervaciu zo zoznamu
        	// metoda Zacni pripravu prostredia NikaWellu() z mojho class diagramu - class Spravca NikaWellu, mojho diagramu aktivit a sekvencneho diagramu
        	private void zacniPripravuProstrediaNikaWellu() {
        	    Rezervacia vybr = listUc03Rezervacie.getSelectedValue();
        	    if (vybr == null) {
        	        JOptionPane.showMessageDialog(this, "Vyberte rezerváciu zo zoznamu.", "Chyba",
        	                JOptionPane.ERROR_MESSAGE);
        	        return;
        	    }

        	    if (maRezervaciaNastaveneParametre(vybr)) {
        	        JOptionPane.showMessageDialog(this,
        	                "Prostredie pre túto rezerváciu už bolo raz pripravené.\n" +
        	                "Nie je možné spustiť prípravu prostredia znova.",
        	                "Informácia",
        	                JOptionPane.INFORMATION_MESSAGE);
        	        return;
        	    }

        	    vybranaRezervacia = vybr;
        	    zobrazDetailRezervacieUc03();
        	    prepnina(KARTA_UC03_DETAIL_REZ);
        	}


        // UC03: Krok 4, Krok 5
        private JPanel vytvorUc03DetailRezPanel() {
            JPanel p = new JPanel(new BorderLayout());
            p.setBackground(new Color(245, 248, 255));

            JLabel hl = new JLabel("Detail rezervácie pre prípravu prostredia", SwingConstants.CENTER);
            hl.setFont(new Font("Segoe UI", Font.BOLD, 22));

            JLabel popis = new JLabel(
                    "Nižšie sú zobrazené všetky údaje rezervácie.",
                    SwingConstants.CENTER);
            popis.setFont(new Font("Segoe UI", Font.PLAIN, 14));

            JPanel top = new JPanel(new GridLayout(2, 1));
            top.setBackground(new Color(245, 248, 255));
            top.add(hl);
            top.add(popis);
            p.add(top, BorderLayout.NORTH);

            taUc03DetailRez = new JEditorPane("text/html", "");
            taUc03DetailRez.setEditable(false);
            taUc03DetailRez.setBackground(new Color(250, 250, 240));
            p.add(new JScrollPane(taUc03DetailRez), BorderLayout.CENTER);

            JPanel tl = new JPanel(new FlowLayout(FlowLayout.CENTER));
            JButton btnSpat = new JButton("Späť");
            styleSecondaryButton(btnSpat);
            JButton btnZacat = new JButton("Začať prípravu prostredia NikaWell-u");
            stylePrimaryButton(btnZacat);

            btnSpat.addActionListener(e -> {
                vybranaRezervacia = null;
                prepnina(KARTA_UC03_ZOZNAM_REZ);
            });
            btnZacat.addActionListener(e -> prepnina(KARTA_UC03_BEZPECNOST));

            tl.add(btnSpat);
            tl.add(btnZacat);
            p.add(tl, BorderLayout.SOUTH);
            return p;
        }

        private void zobrazDetailRezervacieUc03() {
            if (vybranaRezervacia == null) return;
            taUc03DetailRez.setText(buildRezervaciaDetail(vybranaRezervacia));
        }

        // UC03: Krok 6 
        // metoda Vykonaj bezpecnostnu kontrolu miestnosti() z class diagramu - class Spravca NikaWellu a z mojho diagramu aktivit a sekvencneho diagramu
        private JPanel vytvorUc03BezpecnostPanel() {
            JPanel p = new JPanel(new BorderLayout());
            JLabel hl = new JLabel("Bezpečnostná kontrola", SwingConstants.CENTER);
            hl.setFont(new Font("Segoe UI", Font.BOLD, 22));
            p.add(hl, BorderLayout.NORTH);

            lblUc03Bezpecnost = new JLabel(
                    "Prebieha bezpečnostná kontrola miestnosti...",
                    SwingConstants.CENTER);
            lblUc03Bezpecnost.setFont(new Font("Segoe UI", Font.PLAIN, 16));
            p.add(lblUc03Bezpecnost, BorderLayout.CENTER);

            JPanel tl = new JPanel(new FlowLayout(FlowLayout.CENTER));
            JButton btnDalej = new JButton("Pokračovať");
            stylePrimaryButton(btnDalej);
            btnDalej.setVisible(false);
            tl.add(btnDalej);
            p.add(tl, BorderLayout.SOUTH);

            p.addComponentListener(new ComponentAdapter() {
                @Override
                public void componentShown(ComponentEvent e) {
                    lblUc03Bezpecnost.setText(
                            "Prebieha bezpečnostná kontrola miestnosti...");
                    btnDalej.setVisible(false);

                    spravcaNikaWellu.vykonajBezpecnostnuKontroluMiestnosti();

                    javax.swing.Timer t = new javax.swing.Timer(10000, ev -> {
                        lblUc03Bezpecnost.setText("Bezpečnostná kontrola prebehla úspešne.");
                        spravcaNikaWellu.poskytniInformaciuOVysledkuKontroly();
                        btnDalej.setVisible(true);
                    });
                    t.setRepeats(false);
                    t.start();
                }
            });

            btnDalej.addActionListener(e -> prepnina(KARTA_UC03_PARAMETRE));
            return p;
        }

        // UC03: Krok 6, Krok 7, Krok 8
        // metoda Vypln parametre zariadeni() z mojho class diagramu - class Spravca NikaWellu a mojho sekvencneho diagramu a diagramu aktivit
        private JPanel vytvorUc03ParametrePanel() {
            JPanel p = new JPanel(new BorderLayout());
            JLabel hl = new JLabel("Zadanie parametrov prostredia", SwingConstants.CENTER);
            hl.setFont(new Font("Segoe UI", Font.BOLD, 22));
            p.add(hl, BorderLayout.NORTH);

            JPanel center = new JPanel(new GridLayout(5, 2, 5, 5));
            center.setBorder(BorderFactory.createEmptyBorder(20, 200, 20, 200));
            
            // UC03: Krok 7a1
            center.add(new JLabel("Vyberte profil nastavenia prostredia:"));
            cbUc03Profil = new JComboBox<>();
            cbUc03Profil.addItem("Žiadny profil");
            cbUc03Profil.addItem("Dubaj");
            cbUc03Profil.addItem("Relax");
            cbUc03Profil.addItem("Island");
            cbUc03Profil.addItem("Wellness večer");
            center.add(cbUc03Profil);

            center.add(new JLabel("Počet stupňov vo vírivke (°C):"));
            spUc03Vyrivka = new JSpinner(new SpinnerNumberModel(37, 0, 80, 1));
            center.add(spUc03Vyrivka);

            center.add(new JLabel("Počet stupňov v saune (°C):"));
            spUc03Sauna = new JSpinner(new SpinnerNumberModel(90, 0, 150, 1));
            center.add(spUc03Sauna);

            center.add(new JLabel("Počet stupňov v spoločnom priestore (°C):"));
            spUc03Spolocny = new JSpinner(new SpinnerNumberModel(24, 0, 80, 1));
            center.add(spUc03Spolocny);

            JLabel info = new JLabel(
                    "<html>Ak vyberiete profil, parametre sa nastavia automaticky.<br>" +
                            "Pri manuálnom nastavení musí byť dodržaná bezpečnostná hranica (vírivka 20 - 60 °C, sauna 20 - 120 °C, spoločný priestor 20 - 60 °C).</html>",
                    SwingConstants.CENTER);
            info.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            center.add(info);
            center.add(new JLabel(""));

            cbUc03Profil.addActionListener(e -> {
                spravcaProfilov.vyberPozadovanyProfil();
                aktualizujParametrePodlaProfilu();
            });

            JPanel tl = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            JButton btnSpat = new JButton("Späť");
            styleSecondaryButton(btnSpat);
            JButton btnUlozit = new JButton("Uložiť parametre");
            stylePrimaryButton(btnUlozit);

            btnSpat.addActionListener(e -> prepnina(KARTA_UC03_BEZPECNOST));
            btnUlozit.addActionListener(e -> {
                spravcaNikaWellu.vyplnParametreZariadeni();
                ulozParametreUc03();
            });

            tl.add(btnSpat);
            tl.add(btnUlozit);

            p.add(center, BorderLayout.CENTER);
            p.add(tl, BorderLayout.SOUTH);
            return p;
        }

        private void aktualizujParametrePodlaProfilu() {
            String profil = (String) cbUc03Profil.getSelectedItem();
            if ("Dubaj".equals(profil)) {
                spUc03Spolocny.setValue(50);
                spUc03Vyrivka.setValue(40);
                spUc03Sauna.setValue(100);
            } else if ("Relax".equals(profil)) {
                spUc03Spolocny.setValue(24);
                spUc03Vyrivka.setValue(37);
                spUc03Sauna.setValue(80);
            } else if ("Island".equals(profil)) {
                spUc03Spolocny.setValue(20);
                spUc03Vyrivka.setValue(35);
                spUc03Sauna.setValue(110);
            } else if ("Wellness večer".equals(profil)) {
                spUc03Spolocny.setValue(26);
                spUc03Vyrivka.setValue(38);
                spUc03Sauna.setValue(90);
            }
        }

        // UC03: Krok 9
        // metoda Uloz parametre zariadeni v rezervacii() z mojho class diagramu - class Rezervacia a mojho diagramu aktivit a sekvencneho diagramu
        private void ulozParametreUc03() {
            if (vybranaRezervacia == null) {
                JOptionPane.showMessageDialog(this, "Nie je vybraná rezervácia.", "Chyba",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            int tVir = (Integer) spUc03Vyrivka.getValue();
            int tSau = (Integer) spUc03Sauna.getValue();
            int tSpol = (Integer) spUc03Spolocny.getValue();

            if (tVir < 20 || tSau < 20 || tSpol < 20) {
                JOptionPane.showMessageDialog(this,
                        "Teploty musia byť minimálne 20 °C z bezpečnostných dôvodov.",
                        "Chyba bezpečnosti",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (tVir > 60 || tSau > 120 || tSpol > 60) {
                JOptionPane.showMessageDialog(this,
                        "Zadané teploty sú príliš vysoké z bezpečnostných dôvodov " +
                                "(vírivka môže mať maximálne 60 °C, sauna 120 °C, spoločný priestor 60 °C).",
                        "Chyba bezpečnosti",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            Parameter param = new Parameter();
            param.pocetStupnovVoVirivke = tVir;
            param.pocetStupnovVSaune = tSau;
            param.pocetStupnovVSpolocnomPriestore = tSpol;
            vybranaRezervacia.ulozParametreZariadeniVRezervacii(param);

            String profilNazov = (String) cbUc03Profil.getSelectedItem();
            if (!"Žiadny profil".equals(profilNazov)) {
                Profil p = new Profil();
                p.nazov = profilNazov;
                p.parameter = param;
                vybranaRezervacia.profil = p;
            }

            DatabazaSubory.ulozRezervacie(rezervacie);
            spravcaNikaWellu.potvrdVyplneneParametre();
            prepnina(KARTA_UC03_ODOSLAT);
        }

        // UC03: Krok 9, Krok 10, Krok 11
        // metoda Vyber moznost odoslania parametrov zariadeni NikaWellu() z class diagramu - class Spravca NikaWellu, z diagramu aktivit a sekvencneho diagramu
        private JPanel vytvorUc03OdoslatPanel() {
            JPanel p = new JPanel(new BorderLayout());
            JLabel hl = new JLabel("Odoslanie parametrov do zariadení", SwingConstants.CENTER);
            hl.setFont(new Font("Segoe UI", Font.BOLD, 22));
            p.add(hl, BorderLayout.NORTH);

            JLabel msg = new JLabel("Želáte si odoslať parametre do jednotlivých zariadení?",
                    SwingConstants.CENTER);
            msg.setFont(new Font("Segoe UI", Font.PLAIN, 16));
            p.add(msg, BorderLayout.CENTER);

            JPanel tl = new JPanel(new FlowLayout(FlowLayout.CENTER));
            JButton btnNie = new JButton("Nie");
            styleSecondaryButton(btnNie);
            JButton btnAno = new JButton("Áno");
            stylePrimaryButton(btnAno);

            btnNie.addActionListener(e -> {
                vybranaRezervacia = null;
                prepnina(KARTA_DOMOV);
            });
            btnAno.addActionListener(e -> {
                spravcaNikaWellu.vyberMoznostOdoslaniaParametrovZariadeniNikaWellu();
                prepnina(KARTA_UC03_PARAM_NASTAVENE);
            });

            tl.add(btnNie);
            tl.add(btnAno);
            p.add(tl, BorderLayout.SOUTH);
            return p;
        }

        // UC03: Krok 12
        // metoda Nastav parametre na zariadeniach() z class diagramu - class NikaWell, z diagramu aktivit a sekvencneho diagramu
        private JPanel vytvorUc03ParamNastavenePanel() {
            JPanel p = new JPanel(new BorderLayout());
            JLabel hl = new JLabel("NikaWell nastavil parametre", SwingConstants.CENTER);
            hl.setFont(new Font("Segoe UI", Font.BOLD, 22));
            p.add(hl, BorderLayout.NORTH);

            JLabel msg = new JLabel("NikaWell nastavil parametre na zariadeniach.", SwingConstants.CENTER);
            msg.setFont(new Font("Segoe UI", Font.PLAIN, 18));
            p.add(msg, BorderLayout.CENTER);

            JButton btnDalej = new JButton("Pokračovať na evidenciu");
            stylePrimaryButton(btnDalej);
            btnDalej.addActionListener(e -> prepnina(KARTA_UC03_EVIDENCIA));

            JPanel tl = new JPanel();
            tl.add(btnDalej);
            p.add(tl, BorderLayout.SOUTH);
            return p;
        }

        // UC03: Krok 13, Krok 14
        // metoda Zaeviduj informaciu spustenia nastavovania parametrov zariadeni() z class diagramu - class Spravca NikaWellu, z diagramu aktivit a sekvencneho diagramu
        private JPanel vytvorUc03EvidenciaPanel() {
            JPanel p = new JPanel(new BorderLayout());
            JLabel hl = new JLabel("Evidencia spustenia nastavovania parametrov", SwingConstants.CENTER);
            hl.setFont(new Font("Segoe UI", Font.BOLD, 22));
            p.add(hl, BorderLayout.NORTH);

            JLabel msg = new JLabel("Želáte si zaevidovať informáciu o spustení nastavovania parametrov?",
                    SwingConstants.CENTER);
            msg.setFont(new Font("Segoe UI", Font.PLAIN, 16));
            p.add(msg, BorderLayout.CENTER);

            JPanel tl = new JPanel(new FlowLayout(FlowLayout.CENTER));
            JButton btnNie = new JButton("Nie");
            styleSecondaryButton(btnNie);
            JButton btnAno = new JButton("Áno");
            stylePrimaryButton(btnAno);

            btnAno.addActionListener(e -> {
                evidujSpustenieParametrov();
                spravcaNikaWellu.zaevidujInformaciuSpusteniaNastavovaniaParametrovZariadeni();
                prepnina(KARTA_UC03_EVIDENCIA_HOTOVO);
            });

            btnNie.addActionListener(e -> {
                vybranaRezervacia = null;
                prepnina(KARTA_DOMOV);
            });

            tl.add(btnNie);
            tl.add(btnAno);
            p.add(tl, BorderLayout.SOUTH);
            return p;
        }

        private JPanel vytvorUc03EvidenciaMusiPanel() {
            JPanel p = new JPanel(new BorderLayout());
            JLabel hl = new JLabel("Evidencia je povinná", SwingConstants.CENTER);
            hl.setFont(new Font("Segoe UI", Font.BOLD, 22));
            p.add(hl, BorderLayout.NORTH);

            JLabel msg = new JLabel(
                    "<html>Informácia musí byť zaevidovaná.<br>" +
                            "Musíte kliknúť na 'Zaevidovať'.</html>",
                    SwingConstants.CENTER);
            msg.setFont(new Font("Segoe UI", Font.PLAIN, 16));
            p.add(msg, BorderLayout.CENTER);

            JPanel tl = new JPanel();
            JButton btnEvidovat = new JButton("Zaevidovať");
            stylePrimaryButton(btnEvidovat);

            btnEvidovat.addActionListener(e -> {
                evidujSpustenieParametrov();
                spravcaNikaWellu.ulozInformaciuOSpusteniNastavovaniaParametrovNaZariadeniach();
                prepnina(KARTA_UC03_EVIDENCIA_HOTOVO);
            });

            tl.add(btnEvidovat);
            p.add(tl, BorderLayout.SOUTH);
            return p;
        }

        // UC03: Krok 15
        // metoda Uloz informaciu o spusteni nastavovania parametrov na zariadeniach() z class diagramu - class Spravca NikaWellu, z diagramu aktivit a sekvencneho diagramu
        private JPanel vytvorUc03EvidenciaHotovoPanel() {
            JPanel p = new JPanel(new BorderLayout());
            JLabel hl = new JLabel("Evidencia uložená", SwingConstants.CENTER);
            hl.setFont(new Font("Segoe UI", Font.BOLD, 22));
            p.add(hl, BorderLayout.NORTH);

            JLabel msg = new JLabel("Informácia o spustení nastavovania parametrov bola zaevidovaná.",
                    SwingConstants.CENTER);
            msg.setFont(new Font("Segoe UI", Font.PLAIN, 18));
            p.add(msg, BorderLayout.CENTER);

            JButton btnDomov = new JButton("Späť na domovskú obrazovku");
            stylePrimaryButton(btnDomov);
            btnDomov.addActionListener(e -> {
                vybranaRezervacia = null;
                prepnina(KARTA_DOMOV);
            });

            JPanel tl = new JPanel();
            tl.add(btnDomov);
            p.add(tl, BorderLayout.SOUTH);
            return p;
        }

        private void evidujSpustenieParametrov() {
            if (vybranaRezervacia != null) {
                DatabazaSubory.zapisLog(
                        "Spustenie nastavovania parametrov pre rezervaciu s ID = " +
                                vybranaRezervacia.technickeId);
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            HlavneOkno okno = new HlavneOkno();
            okno.setVisible(true);
        });
    }
}