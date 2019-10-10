namespace Mathenger.windows
{
    /// <summary>
    /// Interaction logic for MainWindow.xaml
    /// </summary>
    public partial class MainWindow
    {
        public string Token { get; set; }
        public MainWindow()
        {
            InitializeComponent();
            DataContext = this;
        }
    }
}