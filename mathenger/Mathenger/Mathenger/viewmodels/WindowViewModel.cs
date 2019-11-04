using System.Windows;
using System.Windows.Input;

namespace Mathenger {
  public class WindowViewModel : BaseViewModel {
    private Window _window;
    private int _outerMarginSize = 10;
    private int _windowRadius = 10;
    public WindowViewModel(Window window) {
      _window = window;
      _window.StateChanged += (sender, e) => {
        OnPropertyChanged(nameof(ResizeBorderThickness));
        OnPropertyChanged(nameof(OuterMarginSize));
        OnPropertyChanged(nameof(OuterMarginSizeThickness));
        OnPropertyChanged(nameof(WindowRadius));
        OnPropertyChanged(nameof(WindowCornerRadius));
      };

      MinimizeCommand = new RelayCommand(() => _window.WindowState = WindowState.Maximized);
      MaximizeCommand = new RelayCommand(() => _window.WindowState ^= WindowState.Maximized);
      CloseCommand = new RelayCommand(() => _window.Close());
      MenuCommand = new RelayCommand(() => SystemCommands.ShowSystemMenu(_window, GetMousePosition()));

      WindowResizer resizer = new WindowResizer(_window);
    }
    public int TitleHeight { get; set; } = 40;
    public int OuterMarginSize {
      get => _window.WindowState == WindowState.Maximized ? 0 : _outerMarginSize;
      set => _outerMarginSize = value;
    }
    public int WindowRadius {
      get => _window.WindowState == WindowState.Maximized ? 0 : _windowRadius;
      set => _windowRadius = value;
    }
    public int ResizeBorder { get; set; } = 6;
    public Thickness ResizeBorderThickness => new Thickness(ResizeBorder + OuterMarginSize);
    public Thickness OuterMarginSizeThickness => new Thickness(OuterMarginSize);
    public CornerRadius WindowCornerRadius => new CornerRadius(WindowRadius);
    public GridLength TitleHeightGridLength => new GridLength(TitleHeight + ResizeBorder);

    public ICommand MinimizeCommand { get; set; }
    public ICommand MaximizeCommand { get; set; }
    public ICommand CloseCommand { get; set; }
    public ICommand MenuCommand { get; set; }

    private Point GetMousePosition() {
      Point positionRelativeToScreen = Mouse.GetPosition(_window);
      return new Point(positionRelativeToScreen.X + _window.Left, positionRelativeToScreen.Y + _window.Top);
    }
  }
}
