using Mathenger.services;
using Mathenger.windows;
using Ninject;
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
            _kernel.Bind<ApplicationProperties>().ToSelf().InSingletonScope();
            // Binding services
            _kernel.Bind<AuthenticationService>().ToSelf().InSingletonScope();
            _kernel.Bind<JsonSerializer>().ToSelf().InSingletonScope();
            _kernel.Bind<IRestSerializer>().To<JsonSerializer>();
            // Binding windows
            _kernel.Bind<LoginWindow>().ToSelf().InTransientScope();
        }
    }
}