TeachMint Assignment - Mihir Bajpai

This mobile application allows users to search for GitHub repositories, view details of selected repositories, and explore contributors' repositories. It leverages the GitHub API to provide a seamless experience for users to navigate through repositories and their details.

Home Screen
Search Bar: To search for repositories using the GitHub API.
RecyclerView: Displays search results using CardView.
Pagination: Fetches 10 items per page.
Offline Support: Saves data of the first 15 items offline for accessibility without a network connection.
Jetpack Compose: Used for building UI components.
Connectivity: Check that user is connected with internet or not.

Repo Details Screen
Repository Details: Displays image, name, project link, description, and contributors.
Contributors' Repositories: Lists repositories tagged to the selected contributor.

Technologies and Concepts Used:

- Kotlin
- MVVM Architecture
- Jetpack Compose
- Coroutines
- LiveData
- Room Database
- Retrofit
- RecyclerView
- CardView
- Pagination

Usage

1. Home Screen:
    - Use the search bar to enter a query.
    - View the search results in the RecyclerView.
    - Click on a repository to navigate to the Repo Details screen.
2. Repo Details Screen:
    - View detailed information about the selected repository.
    - Click on the project link to open it in a WebView.
    - View and navigate through the list of contributors and their repositories.

For any inquiries or feedback, please contact Mihir Bajpai at themihirbajpai@gmail.com.


Additional Implementation Notes

- Search Implementation: Utilize Retrofit for making API calls to GitHub. Use Coroutines for handling asynchronous operations.
- Offline Support: Implement Room Database to store the first 15 items fetched from the API.
- Pagination: Implement pagination logic to load more data as the user scrolls.
- Jetpack Compose: Use Compose to create a modern and responsive UI for both the Home and Repo Details screens.

