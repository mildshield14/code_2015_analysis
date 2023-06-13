import random

def generate_dataset(num_samples):
    dataset = []
    for i in range(1, num_samples + 1):
        lat = round(random.uniform(45.0,46.0), 4)
        lon = round(random.uniform(-74.0,-73.0), 4)
        dataset.append((i, (lat,lon)))
    return dataset

# Generate a sample dataset with 10 coordinates
sample_dataset = generate_dataset(100)

# Save the sample dataset to a text file
with open("ress.txt", "w") as file:
    file.write("3400 5600 \n")
    for entry in sample_dataset:
        file.write(f"{entry[0]}{entry[1]}\n")

# Close the file
file.close()

print("Sample dataset saved to res.txt")

