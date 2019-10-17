using Mathenger.pages;
using Mathenger.services;
using Mathenger.windows;
using Mathenger.windows.dialogs;
using Ninject;
using RestSharp;
using RestSharp.Serialization;

namespace Mathenger.config
{
    public static class IoC
    {
        private static IKernel _kernel = new StandardKernel();

        public static T Get<T>()
        {
            return _kernel.Get<T>();
        }

        public static void Setup()
        {
            //Binding configs
            _kernel.Bind<ApplicationProperties>().ToSelf().InSingletonScope();
            _kernel.Bind<RequestSender>().ToSelf().InSingletonScope();
            _kernel.Bind<UnAuthorizedHandler>().ToSelf().InSingletonScope();
            // Binding services
            _kernel.Bind<IRestClient>()
                .ToConstructor(arg => new RestClient(arg.Inject<string>()))
                .InSingletonScope().WithConstructorArgument("baseUrl",
                    context => context.Kernel.Get<ApplicationProperties>().ApiBaseUrl);
            _kernel.Bind<AuthenticationService>().ToSelf().InSingletonScope();
            _kernel.Bind<IRestSerializer>().To<JsonSerializer>().InSingletonScope();
            _kernel.Bind<AccountService>().ToSelf().InSingletonScope();
            _kernel.Bind<ChatService>().ToSelf().InSingletonScope();
            // Binding windows
            _kernel.Bind<LoginWindow>().ToSelf().InTransientScope();
            _kernel.Bind<MainWindow>().ToSelf().InTransientScope();
            _kernel.Bind<AddContactDialog>().ToSelf().InTransientScope();
            // Binding pages
            _kernel.Bind<SignInPage>().ToSelf().InTransientScope();
            _kernel.Bind<SignUpPage>().ToSelf().InTransientScope();
        }
    }
}