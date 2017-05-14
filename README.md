# root-gist
Track driving history for people.

<hr>

## Design Approach
My main though process in providing this solution was extensibility and maintainability: I wanted to make it easy to plug-in new commands other than `Driver` and `Trip`, and to make the addition of those new commands have a natural place in the project with minimal to no need for modifying the main application to handle a new command.

To this end, I took a strategy approach, encapsulating the strategy for each command in an enum; this is the natural place in the project in which to add new commands.  The main class, `DriverTracker`, orchestrates all the necessary logic to parse the input file, process its data, and run each command, without needing to know the details of any particular line or command logic.  Even the `RootDriverApplication` bootstrap class uses the CLI library, allowing for easy plug-and-play of additional command-line arguments with descriptions and validation.

I tend to prefer strongly-typed objects (particularly coming from such a strong Java background), so after parsing, I instantiate POJOs representing the data I just parsed.  This allows me to keep the data immutable, when possible, checking all invariants at creation-time, and dealing with bad data up-front so that the ensuing application logic _knows_ that it has what it needs to perform its function.

I try to encapsulate logic and data in the objects to which they belong; if a `Trip` class contains its own consituent components, then it can also calculate its own average speed. 

## Additional Considerations

### Fail-fast
I am a strong proponent of the fail-fast paradigm, which allows the developer to know, as early as possible, when an invariant or expectation is not met.  This aids in quick development, makes writing tests easier, and acts as additional documentation.

### Documentation
>Good code is good documentation

While true, this really only goes so far.  It does nothing for the public consumers of such code, and doesn't help in calling out subtle side-effects or odd business logic.

So, while I do believe that "good code is good documentation", I also love javadocs and I love inline code comments.  Public methods and classes have javadocs, allowing public consumers to know the intended effect of that code.  Inline comments occur when I want to drive a point home, or when I want to make clear something that my code invocations did not.
