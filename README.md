# Spidertracks CRM?

This android application allows a company to view and manage customer information. An API has been deployed for the purpose and the application should have the ability to view, filter and sort customers, view customer details and add/edit sales opportunities for each customer. 

## Feel the app before dive 🥳
[Download APK](https://github.com/DimalPerera/spidertracks_crm/blob/master/apk/spidertrack-crm-debug.apk)

## App Features
**Customer**
* View customer list
* Filter customers by status (Active, Non-Active, Lead)
* Add new customer to the system
* Update customer details *(To update the Customer details, long press the customer list item)*
* Remove existing customers 
 
**Opportunity**
* View customer's opportunities
* Add new opportunity to the customer
* Update opportunity *((To update the Customer details, long press the opportunity list item)*
* Remove existing opportunity 

## Screenshots

|Customer List|Filter|Add|Update (Long press) |Remove
| --- | --- | --- | --- | --- |
|<img width="200" height="360" src="https://github.com/DimalPerera/spidertracks_crm/blob/master/screens/1.jpeg"/>|<img width="200" height="360" src="https://github.com/DimalPerera/spidertracks_crm/blob/master/screens/2.jpeg"/>|<img width="200" height="360" src="https://github.com/DimalPerera/spidertracks_crm/blob/master/screens/3.jpeg"/>|<img width="200" height="360" src="https://github.com/DimalPerera/spidertracks_crm/blob/master/screens/5.jpeg"/>|<img width="200" height="360" src="https://github.com/DimalPerera/spidertracks_crm/blob/master/screens/4.jpeg"/>

|Opportunity List|Add|Update (Long press) |Remove
| --- | --- | --- | --- |
|<img width="200" height="360" src="https://github.com/DimalPerera/spidertracks_crm/blob/master/screens/6.jpeg"/>|<img width="200" height="360" src="https://github.com/DimalPerera/spidertracks_crm/blob/master/screens/7.jpeg"/>|<img width="200" height="360" src="https://github.com/DimalPerera/spidertracks_crm/blob/master/screens/8.jpeg"/>|<img width="200" height="360" src="https://github.com/DimalPerera/spidertracks_crm/blob/master/screens/9.jpeg"/>



## Tech

**Why MVVM?**

MVVM (Model-View-ViewModel) is an architectural pattern and it provides a clear separation of concerns between the user interface (View), the data and business logic (Model), and the intermediary that binds them together (ViewModel). The ViewModel acts as a bridge between the View and the Model, exposing properties and commands that the View can bind to, and handling any data-related logic or operations. This allows for a more modular and testable codebase and makes it easier to update or replace parts of the application without affecting the others.
 
**MVVM Best Pratice:**
- Avoid references to Views in ViewModels.
- Instead of pushing data to the UI, let the UI observe changes to it. (LiveData)
- Distribute responsibilities, add a domain layer if needed.
- Add a data repository as the single-point entry to your data.
- Expose information about the state of your data using a wrapper or another LiveData.
- Consider edge cases, leaks and how long-running operations can affect the instances in your architecture.
- Don’t put logic in the ViewModel that is critical to saving clean state or related to data. Any call you make from a ViewModel can be the last one.

**Repository pattern**

MVVM (Model-View-ViewModel) is an architectural pattern that separates the user interface from the data and business logic in an application. The ViewModel acts as a bridge between the View and the Model, and the repository pattern is used to abstract away the data source and provide a single point of access for the application's data. When used together in Android development, MVVM and the repository pattern can help create a clean, modular, and testable architecture.

![mvvm2](https://user-images.githubusercontent.com/1812129/68319008-e9d39d00-00bd-11ea-9245-ebedd2a2c067.png)


**App third-party dependencies**

`Retrofit` - Retrofit is a type-safe HTTP client for Android and Java developed by Square. It simplifies the process of sending network requests and handling responses, making it easier to work with RESTful APIs.

`Jackson` - Jackson is a JSON parser library for Android that allows for the Serializing and deserializing JSON data. It is a high-performance library that is widely used in the Android ecosystem for its ease of use and flexibility.

`RxJava` - RxJava is a Java library for implementing reactive programming, which allows for more efficient handling of asynchronous data streams.

`Mockito` - Mockito is a library for creating test doubles (mocks, stubs, and spies) in Android and Java. It is used for isolating the code under test and for testing interactions between different components of an application in a controlled way.

## Where to start

**Creating Model Class**
```kotlin
import com.fasterxml.jackson.annotation.JsonProperty
import kotlinx.parcelize.Parcelize
import android.os.Parcelable

@Parcelize
data class Customer(
    @JsonProperty("id")
    var id: Int?, // 1
    @JsonProperty("createdDate")
    var createdDate: String?, // 2022-10-07T11:18:00
    @JsonProperty("email")
    var email: String?, // hscherme5a@aol.com
    @JsonProperty("name")
    var name: String?, // Myca Blanchflower
    @JsonProperty("phoneNumber")
    var phoneNumber: String?, // (555) 857-1351
    @JsonProperty("status")
    var status: String? // active
) : Parcelable
```

**Retrofit Request method (ApiService Interface)**
```kotlin
interface ApiService {
    @GET("customers")
    fun getCustomers(): Single<CustomerList>?
}
```
**Repository implementation - RxJava (UserRepository)**
```kotlin
fun getCustomersFromApi(): Single<CustomerList>?{
        return apiService.getCustomers()
            ?.subscribeOn(Schedulers.io())
            ?.observeOn(AndroidSchedulers.mainThread())

    }
```
**Viewmodel implementation (CustomerViewModel)**
```kotlin
private val _items = MutableLiveData<List<Customer>>()
val items: LiveData<List<Customer>> = _items
    
fun fetchCustomerList() {
        _progressBar.value = true

        repository.getCustomersFromApi()
            ?.subscribe({ items ->
                _progressBar.value = false
                _items.value = items
            }, {
                _progressBar.value = false
                _message.value = it.message
            })
    }
```

**Activity/ Fragment implementation**
```kotlin
viewModel.items.observe(viewLifecycleOwner, Observer {
            adapter.updateItems(it)
})
```

## Project APIs

| Endpoint | Description |
| --- | --- |
| GET/customers | Returns a list of all customers |
| GET/customers/{id} | Returns a single customer |
| POST/customers | Creates a new customer, use a JSONobject with the customer shape as body|
| PUT/customers/{id} | Updates a customer, use a JSON objectwith the customer shape as body|
| DELETE/customers/{id} | Deletes a customer |
| GET/customers/{id}/opportunities/ | Returns all opportunities for the customer |
| GET/customers/{id}/opportunities/{opId} | Returns the opportunity for the customer |
| POST/customers/{id}/opportunities/ | Creates a new opportunity, use a JSONobject with the opportunity shape as body |
|PUT/customers/{id}/opportunities/{opId}|Updates an opportunity, use a JSON object with the opportunity shape as body|
|DELETE/customers/{id}/opportunities/{opId}|Deletes an opportunity|


## *TODO*
- [x] Check network availability before executing the HTTP request and show error message to the user
- [x] LiveData observer is being triggered multiple times
- [x] Show proper messages to the users about API request Success or Fail
