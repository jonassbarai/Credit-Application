package me.dio.creditapplicationsystem.entity

import jakarta.persistence.*

@Entity
@Table(name = "Customer_table")
data class Customer(
    @Column(nullable = false) var firstName: String = "",
    @Column(nullable = false) var lastName: String = "",
    @Column(nullable = false, unique = true) val cpf: String = "",
    @Column(nullable = false, unique = true) var email: String = "",
    @Column(nullable = false) var password: String = "",
    @Column(nullable = false) @Embedded var adress: Adress = Adress(),
    @Column(nullable = false)
    @OneToMany(fetch = FetchType.LAZY,
        cascade = arrayOf(CascadeType.REMOVE,CascadeType.PERSIST),
        mappedBy = "customer")
    var credit: List<Credit> = mutableListOf(),
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) var id: Long? = null
)
