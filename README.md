# IF3210-2020-BolaSepak


## Deskripsi aplikasi
BolaSepak merupakan sebuah aplikasi berbasis android dengan fitur utama berupa 
pemberitahuan jadwal dan pertandingan, statistik pertandingan, dan track record 
suatu team. Pada aplikasi ini, pengguna dapat mensubscribe terhadap suatu tim 
sepak bola pilihan-nya dan dengan mensubscribe, pengguna akan menerima 
notifikasi apabila tim tersebut akan bertanding. Selain itu, juga terdapat 
beberapa fitur tambahan, yaitu fitur pencarian pertandingan dan tracking jumlah
langkah yang telah dilakukan oleh pengguna setiap harinya.

## Cara kerja, terutama mengenai pemenuhan spesifikasi aplikasi
### Fitur Schedule
    1. Dalam mendapatkan jadwal terkini, kami menggunakan API dari thesportsdb
    2. Kami mengambil data dari API dengan menggunakan JSONObjectRequest yang
    nantinya akan diproses sehingga dapat dimasukkan ke dalam template schedule
    yang telah dibuat
    3. Kami melakukan 3 kali request, request pertama untuk mengambil idTeam
    dan logo team, request kedua untuk mengambil jadwal pertandingan yang telah
    usai, dan request ketiga untuk mengambil jadwal pertandingan yang akan 
    datang
    4. Template dibuat dengan menggunakan card view yang nantinya akan ditempel
    ke recycler view menggunakan adapter
    5. Adapter juga berfungsi untuk memasukan data-data yang telah diambil ke
    dalam template
    
## Library yang digunakan dan justifikasi penggunaannya
## Screenshot aplikasi







#### 13517005 - Muhammad Rafi Zhafran
#### 13517116 - Ferdy Santoso
#### 13517128 - Yudy Valentino


