package se.wigell.biluthyrning.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "booking")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})

public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Integer active;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "car_id")
    private Car car;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;

    private LocalDate from_date;
    private LocalDate to_date;
    private Double total_price;

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public Integer getActive() { return active; }
    public void setActive(Integer active) { this.active = active; }

    public Car getCar() { return car; }
    public void setCar(Car car) { this.car = car; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public LocalDate getFrom_date() { return from_date; }
    public void setFrom_date(LocalDate from_date) { this.from_date = from_date; }

    public LocalDate getTo_date() { return to_date; }
    public void setTo_date(LocalDate to_date) { this.to_date = to_date; }

    public Double getTotal_price() { return total_price; }
    public void setTotal_price(Double total_price) { this.total_price = total_price; }
}
