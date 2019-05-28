# Notes

- I added a grid toggle option for the deal list. It doesn't use a different layout between the linear list layout, as I wasn't able to come up with a new layout significantly more useful. The grid layout option only appears if the device's width is enough to support showing at least two columns.

- The deal list has a few sort options and can be filtered by a search term that is applied to the deal's title.

- There is a shopping cart and a tab to view and modify it.

- This app uses Realm for persistence storage of the deals and shopping cart contents. I also leverage the sorting and searching capability of Realm for the deals list. An idea for further improvement would be to wrap Realm so it uses LiveData and Android Architecture Components to manage the lifecycle. I have only started playing around with the architecture components so I didn't feel comfortable including them.

- The prices returned by the API for each product are converted to actual integers before stored in Realm. This allows for easier sorting and easier calcuations for the shopping cart.
