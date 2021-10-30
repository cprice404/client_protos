#!/bin/bash
set -e
set -x

# Generate python proto artifacts
python -m grpc_tools.protoc -I../proto --python_out=src/momento_wire_types --grpc_python_out=src/momento_wire_types cacheclient.proto controlclient.proto

# Protobuf imports generated by Google are broken and need manual fixing
# https://github.com/protocolbuffers/protobuf/issues/1491
#
# Keep a backup of auto-generated files to allow for debugging.
mv src/momento_wire_types/cacheclient_pb2_grpc.py src/momento_wire_types/cacheclient_pb2_grpc.py.old
mv src/momento_wire_types/controlclient_pb2_grpc.py src/momento_wire_types/controlclient_pb2_grpc.py.old
#
# Replace `import cacheclient_pb2 as cacheclient__pb2` with `from . import cacheclient_pb2 as cacheclient__pb2`
sed -e 's/^import cacheclient_pb2 as cacheclient__pb2$/from . import cacheclient_pb2 as cacheclient__pb2/' src/momento_wire_types/cacheclient_pb2_grpc.py.old > src/momento_wire_types/cacheclient_pb2_grpc.py
# Replace `import controlclient_pb2 as controlclient__pb2` with `from . import controlclient_pb2 as controlclient__pb2`
sed -e 's/^import controlclient_pb2 as controlclient__pb2$/from . import controlclient_pb2 as controlclient__pb2/' src/momento_wire_types/controlclient_pb2_grpc.py.old > src/momento_wire_types/controlclient_pb2_grpc.py

# Build the module
python -m build
