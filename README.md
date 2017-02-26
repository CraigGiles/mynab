# MyNAB
You Need A Budget (YNAB) has been one of the best budgeting tools I've used to ensure my personal finances are on track. The classic version of YNAB has been discontinued in favor of a web based version so updates will be few. I took this opportunity to start developing my own budgeting application that fit my needs and help improve my large project organization.

## Design
The systems design is broken into various sub-projects to promote clear separation of concerns.

* Domain: Trying to be as purely functional as I can with this layer. The domain layer houses information on the domain of "what is budgetting" in the MyNAB world.
* Backend: The back end is something like an aggregation layer for the domain and persistence layers. It may delegate responsibilities down to the domain layer for manipulation of ideas and call out to the persistence layer for stateful changes before sending the information back up to the presentation layer.
* Http4s: Currently un-used, this is one presentation layer that can be used. Current ideas is that the web layer could function as a typical presentation if the system were to be used on a web server. Other presentation layers could be hooked into the system including a desktop application. These layers will talk to the `Backend`
* Mysql: The persistence layer for the application.
* Flyway: The persistence layers migration system.

## Usage
Because the project is currently in development, there are no deliverable artifacts that ban be run directly. Running the unit test suite will give you a small idea on the features that currently exist in this system.

## Contributing
Current Code Coverage Metrics:

```
[ Domain ]
---------------------------
Statement coverage.: 54.00%
Branch coverage....: 75.00%
Coverage reports completed
All done. Coverage was [54.00%]
```

```
[ Backend ]
---------------------------
Statement coverage.: 71.76%
Branch coverage....: 100.00%
Coverage reports completed
All done. Coverage was [71.76%]
```


## Credits
Currently all work is being done by myself ([Craig Giles](http://www.github.com/craiggiles)) in my spare time.

## License
MIT License

Copyright (c) 2017 Craig Giles

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.

---

### [LATEST]
#### Added
#### Fixed
#### Removed

