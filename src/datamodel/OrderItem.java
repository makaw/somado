/*
 * 
 *  Somado (System Optymalizacji Małych Dostaw)
 *  Program jest częścią pracy dyplomowej inżynierskiej zrealizowanej
 *  na Wydziale Elektrycznym Politechniki Warszawskiej.
 *  Autor: Maciej Kawecki 2016
 * 
 */
package datamodel;


/**
 *
 * Szablon obiektu reprezentującego pozycję w zamówieniu
 * 
 * @author Maciej Kawecki
 * @version 1.0
 * 
 */
public class OrderItem {

  /** Produkt */
  private final Product product;
  /** Ilość sztuk produktu */
  private final int itemsNumber;
  /** Łączna waga */
  private final double totalWeight;
  
  
  /**
   * Konstruktor
   * @param product Produkt
   * @param itemsNumber Ilość sztuk produktu
   */
  public OrderItem(Product product, int itemsNumber) {
      
    this.product = product;
    this.itemsNumber = itemsNumber;
    this.totalWeight = (product == null) ? 0.0 : product.getWeight() * itemsNumber;    
      
  }
  
  /**
   * Konstruktor dla pustej listy 
   */
  public OrderItem() {
       
    this(new Product(), 0);
      
  }  
  

  public Product getProduct() {
     return product;
  }

  public int getItemsNumber() {
     return itemsNumber;
  }

  public double getTotalWeight() {
     return totalWeight;
  }      
  
  
  @Override
  public String toString() {
      
    return String.valueOf(itemsNumber) + " \u00d7 " + product.toString();
      
  }
 
   
}
