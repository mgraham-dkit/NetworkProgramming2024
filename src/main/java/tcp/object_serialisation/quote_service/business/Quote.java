package tcp.object_serialisation.quote_service.business;

import java.io.*;
import java.util.Objects;

/**
 *
 * @author michelle
 */
public class Quote implements Serializable {
    private String quotationText;
    private String author;

    public Quote(String quotationText, String author) {
        this.quotationText = quotationText;
        this.author = author;
    }

    public String getQuotationText() {
        return quotationText;
    }

    public void setQuotationText(String quotationText) {
        this.quotationText = quotationText;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 97 * hash + Objects.hashCode(this.quotationText);
        hash = 97 * hash + Objects.hashCode(this.author);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Quote other = (Quote) obj;
        if (!Objects.equals(this.quotationText, other.quotationText)) {
            return false;
        }
        if (!Objects.equals(this.author, other.author)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Quote{" + "quotationText=" + quotationText + ", author=" + author + '}';
    }

    public String encode(String delimiter) {
        return this.quotationText + delimiter + this.author;
    }

    public static Quote decode(String encoded, String delimiter) {
        String[] components = encoded.split(delimiter);
        if (components.length != 2) {
            return null;
        }

        return new Quote(components[0], components[1]);
    }

    public static void main(String[] args) {
        Quote test = new Quote("Oh, biscuits!", "Bandit Heeler");
        try (FileOutputStream output = new FileOutputStream("SampleOutput/objects.txt")) {
            try (ObjectOutputStream oos = new ObjectOutputStream(output)) {
                oos.writeObject(test);
                oos.flush();
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        try(FileInputStream input = new FileInputStream("SampleOutput/objects.txt")){
            try(ObjectInputStream ois = new ObjectInputStream(input)){
                Object fileData = ois.readObject();
                if(fileData instanceof Quote){
                    Quote received = (Quote) fileData;
                    System.out.println(received);
                }else{
                    System.out.println("unrecognised object read in!");
                }
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}