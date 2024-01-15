from pytube import YouTube
import os
import re

def list_available_resolutions(streams):
    resolutions = []
    for stream in streams:
        if stream.resolution and stream.resolution not in resolutions:
            resolutions.append(stream.resolution)
    return sorted(resolutions, key=lambda x: int(x[:-1]), reverse=True)

def sanitize_filename(filename):
    # Remove invalid characters for file names
    return re.sub(r'[\\/*?:"<>|]', '', filename)

def download_youtube_video(yt, resolution, path='.'):
    try:
        # Select the stream with the specified resolution
        video_stream = yt.streams.filter(res=resolution, file_extension='mp4').first()
        if not video_stream:
            raise Exception(f"No video stream available for resolution {resolution}")

        # Sanitize and prepare the filename
        sanitized_title = sanitize_filename(yt.title)
        base_filename = f"{sanitized_title} {resolution}"
        filename = f"{base_filename}.mp4"
        counter = 1

        # Check if the file exists
        while os.path.exists(os.path.join(path, filename)):
            user_input = input(f"{filename} already exists. Do you want to download it again? [y/n]: ").lower()
            if user_input not in ['y', 'yes']:
                return

            filename = f"{base_filename} ({counter}).mp4"
            counter += 1

        # Download the video
        video_stream.download(output_path=path, filename=filename)
        print(f"Video downloaded successfully: {filename}")
    except Exception as e:
        print(f"An error occurred: {e}")

def main():
    # Ask the user for the URL
    url = input("Enter the URL of the YouTube video you want to download: ")
    yt = YouTube(url)

    # List available resolutions
    available_resolutions = list_available_resolutions(yt.streams)
    print("Available resolutions:")
    for res in available_resolutions:
        print(res)

    while True:
        # Ask the user to select a resolution or 'all' for all resolutions
        selected_resolution = input("Select the resolution (type 'all' for all resolutions, 'exit' or 'done' to quit): ")
        if selected_resolution.lower() == 'exit' or selected_resolution.lower() == 'done':
            break

        if selected_resolution.lower() == 'all':
            for res in available_resolutions:
                download_youtube_video(yt, res)
            break

        # Append 'p' if it's missing
        if not selected_resolution.endswith('p'):
            selected_resolution += 'p'

        if selected_resolution in available_resolutions:
            download_youtube_video(yt, selected_resolution)
        else:
            print("Please select a resolution from the available resolutions or type 'all'.")

if __name__ == "__main__":
    main()
